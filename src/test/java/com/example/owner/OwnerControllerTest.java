package com.example.owner;

import com.example.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OwnerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OwnerRepository ownerRepository;

    private final Pageable pageable = PageRequest.of(0, 3);

    private List<Owner> owners;

    @BeforeEach
    void setUp() {

        owners = Utils.generateOwners();

        Mockito.reset(ownerRepository);

        when(ownerRepository.findPetTypes()).thenReturn(owners.stream()
                .map(Owner::getPets)
                .flatMap(List::stream)
                .map(Pet::getType)
                .distinct()
                .toList());

        when(ownerRepository.findById(-1)).thenReturn(Optional.empty());
        when(ownerRepository.findByNameLike("", pageable)).thenReturn(Page.empty());
        when(ownerRepository.findAll()).thenReturn(owners);
        when(ownerRepository.findAll(pageable)).thenReturn(new PageImpl<>(owners));

        for (var owner : owners) {
            when(ownerRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
            when(ownerRepository.findByNameLike(owner.getFirstName(), Pageable.ofSize(20))).thenReturn(new PageImpl<>(List.of(owner)));
            when(ownerRepository.findByNameLike(owner.getLastName(), Pageable.ofSize(20))).thenReturn(new PageImpl<>(List.of(owner)));
            when(ownerRepository.save(owner)).thenReturn(owner);
        }
    }

    @Test
    void testShowOwner() throws Exception {

        var owner = owners.get(0);

        mvc.perform(get("/owners/{id}", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attribute("owner", owner))
                .andExpect(content().string(containsString(owner.getFirstName())))
                .andExpect(content().string(containsString(owner.getLastName())))
                .andExpect(content().string(containsString("Edit owner")));

        verify(ownerRepository, times(1)).findById(owner.getId());
    }

    @Test
    void testShowOwners() throws Exception {

        String content = mvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("owners", new PageImpl<>(owners)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        for (var owner : owners) {
            assertTrue(content.contains(owner.getFirstName()));
            assertTrue(content.contains(owner.getLastName()));
        }

        verify(ownerRepository, times(1)).findAll(pageable);
    }

    @Test
    void testProcessFindForm() throws Exception {

        var owner = owners.get(0);

        mvc.perform(get("/owners/find?text={text}", owner.getFirstName()))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(content().string(containsString(owner.getFirstName())))
                .andExpect(content().string(containsString(owner.getLastName())));

        mvc.perform(get("/owners/find?text={text}", " "))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/owners"));

        verify(ownerRepository, times(1)).findByNameLike(any(String.class), any(Pageable.class));
        verify(ownerRepository, times(1)).findByNameLike(owner.getFirstName(), Pageable.ofSize(20));
    }

    @Test
    void testInitCreationForm() throws Exception {

        mvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attribute("owner", new Owner()))
                .andExpect(content().string(containsString("Add owner")));
    }

    @Test
    void testProcessCreationForm() throws Exception {

        var newOwner = new Owner();
        newOwner.setFirstName("new owner first name");
        newOwner.setLastName("new owner last name");
        newOwner.setAddress("new owner address");
        newOwner.setCity("new owner city");
        newOwner.setTelephone("2342342342");

        mvc.perform(post("/owners/new")
                        .param("firstName", newOwner.getFirstName())
                        .param("lastName", newOwner.getLastName())
                        .param("address", newOwner.getAddress())
                        .param("city", newOwner.getCity())
                        .param("telephone", newOwner.getTelephone()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/owners"));

        mvc.perform(post("/owners/new")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("address", "")
                        .param("city", "")
                        .param("telephone", "abcdefg"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));

        verify(ownerRepository, times(1)).save(any(Owner.class));
        verify(ownerRepository, times(1)).save(newOwner);
    }

    @Test
    void testInitUpdateForm() throws Exception {

        var owner = owners.get(0);

        mvc.perform(get("/owners/{id}/edit", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attribute("owner", owner))
                .andExpect(content().string(containsString("Update owner")));

        verify(ownerRepository, times(1)).findById(owner.getId());
    }

    @Test
    void testProcessUpdateForm() throws Exception {

        var owner = owners.get(0);

        mvc.perform(post("/owners/{id}/edit", owner.getId())
                        .param("firstName", owner.getFirstName() + " updated")
                        .param("lastName", owner.getLastName() + " updated")
                        .param("address", owner.getAddress() + " updated")
                        .param("city", owner.getCity() + " updated")
                        .param("telephone", owner.getTelephone() + "1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/owners/" + owner.getId()));

        mvc.perform(post("/owners/{id}/edit", owner.getId())
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("address", "")
                        .param("city", "")
                        .param("telephone", "abcdefg"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));

        verify(ownerRepository, times(1)).save(any(Owner.class));
    }
}