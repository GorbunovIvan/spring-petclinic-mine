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

import java.time.LocalDate;
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
class PetControllerTest {

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

        mvc.perform(get("/owners/{ownerId}/pets/new", owner.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"))
                .andExpect(model().attribute("pet", new Pet()))
                .andExpect(content().string(containsString("Add pet")));

        verify(ownerRepository, times(1)).findById(owner.getId());
    }

    @Test
    void testProcessCreationForm() throws Exception {

        var owner = owners.get(0);

        var newPet = new Pet();
        newPet.setName("new pet name");
        newPet.setType(new PetType("new type"));
        newPet.setBirthDate(LocalDate.now());

        mvc.perform(post("/owners/{ownerId}/pets/new", owner.getId())
                        .param("name", newPet.getName())
                        .param("type", newPet.getType().getName())
                        .param("birthDate", newPet.getBirthDate().toString()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/owners/" + owner.getId()));

        mvc.perform(post("/owners/{ownerId}/pets/new", owner.getId())
                        .param("name", "")
                        .param("type", "")
                        .param("birthDate", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"));

        verify(ownerRepository, times(2)).findById(owner.getId());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    void testInitUpdateForm() throws Exception {

        var owner = owners.get(0);
        var pet = owner.getPets().iterator().next();

        mvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", owner.getId(), pet.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"))
                .andExpect(model().attribute("pet", pet))
                .andExpect(content().string(containsString("Update pet")));

        verify(ownerRepository, times(1)).findById(owner.getId());
    }

    @Test
    void testProcessUpdateForm() throws Exception {

        var owner = owners.get(0);
        var pet = owner.getPets().iterator().next();

        mvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", owner.getId(), pet.getId())
                    .param("name", pet.getName() + " updated")
                    .param("type", pet.getType().getName() + " updated")
                    .param("birthDate", pet.getBirthDate().toString()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/owners/" + owner.getId()));

        mvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", owner.getId(), pet.getId())
                        .param("name", "")
                        .param("type", "")
                        .param("birthDate", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"));

        verify(ownerRepository, times(2)).findById(owner.getId());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }
}