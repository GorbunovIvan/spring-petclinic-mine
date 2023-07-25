package com.example.vet;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VetRepository vetRepository;

    private List<Vet> vets;

    @BeforeEach
    void setUp() {

        vets = Utils.generateVets();

        Mockito.reset(vetRepository);

        when(vetRepository.findAll()).thenReturn(vets);
    }

    @Test
    void showVetList() throws Exception {

        String content = mvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("vets", vets))
                .andExpect(view().name("vets/vetList"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        for (var vet : vets) {
            assertTrue(content.contains(vet.getFirstName()));
            assertTrue(content.contains(vet.getLastName()));
        }

        verify(vetRepository, times(1)).findAll();
    }
}