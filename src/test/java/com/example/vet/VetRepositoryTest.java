package com.example.vet;

import com.example.Utils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VetRepositoryTest {

    @Autowired
    private VetRepository vetRepository;

    private List<Vet> vets = new ArrayList<>();

    @BeforeEach
    void setUp() {
        var vetsList = Utils.generateVets();
        vets = vetRepository.saveAll(vetsList);
    }

    @Test
    @Order(1)
    void findAll() {
        assertEquals(vets, vetRepository.findAll());
    }
}