package com.example.owner;

import com.example.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VisitControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OwnerRepository ownerRepository;

    private List<Owner> owners;

    @BeforeEach
    void setUp() {

        owners = Utils.generateOwners();

        Mockito.reset(ownerRepository);

        when(ownerRepository.findById(-1)).thenReturn(Optional.empty());

        for (var owner : owners) {
            when(ownerRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
            when(ownerRepository.save(owner)).thenReturn(owner);
        }
    }

    @Test
    void testInitCreationForm() throws Exception {

        var owner = owners.get(0);
        var pet = owner.getPets().iterator().next();

        mvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdateVisitForm"))
                .andExpect(model().attribute("visit", new Visit()))
                .andExpect(content().string(containsString("Add visit")));

        verify(ownerRepository, times(1)).findById(owner.getId());
    }

    @Test
    void testProcessCreationForm() throws Exception {

        var owner = owners.get(0);
        var pet = owner.getPets().iterator().next();

        var newVisit = new Visit();
        newVisit.setDescription("new visit description");

        mvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId())
                        .param("description", newVisit.getDescription())
                        .param("date", newVisit.getDate().toString()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/owners/" + owner.getId()));

        mvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId())
                        .param("description", "")
                        .param("date", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdateVisitForm"))
                .andExpect(content().string(containsString("Add visit")));

        verify(ownerRepository, times(2)).findById(owner.getId());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }
}