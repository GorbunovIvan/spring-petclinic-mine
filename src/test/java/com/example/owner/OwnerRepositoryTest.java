package com.example.owner;

import com.example.Utils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    private List<Owner> owners = new ArrayList<>();

    private final Pageable pageable = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {
        var ownersList = Utils.generateOwners();
        owners = ownerRepository.saveAll(ownersList);
    }

    @Test
    @Order(1)
    void testFindPetTypes() {

        var petTypes = ownerRepository.findPetTypes();

        var petTypesExpected = owners.stream()
                .map(Owner::getPets)
                .flatMap(List::stream)
                .map(Pet::getType)
                .distinct()
                .toList();

        assertEquals(petTypesExpected, petTypes);
    }

    @Test
    @Order(2)
    void testFindByNameLike() {

        for (var owner : owners) {
            assertEquals(List.of(owner), ownerRepository.findByNameLike(owner.getFirstName(), pageable).getContent());
            assertEquals(List.of(owner), ownerRepository.findByNameLike(owner.getLastName(), pageable).getContent());
        }

        assertEquals(owners, ownerRepository.findByNameLike("test", pageable).getContent());
        assertEquals(Collections.emptyList(), ownerRepository.findByNameLike("none", pageable).getContent());

    }

    @Test
    @Order(3)
    void testFindById() {

        for (var owner : owners) {
            var ownerOpt = ownerRepository.findById(owner.getId());
            assertTrue(ownerOpt.isPresent());
            assertEquals(owner, ownerOpt.get());
        }

        assertTrue(ownerRepository.findById(-1).isEmpty());
    }

    @Test
    @Order(4)
    void testFindAll() {
        assertEquals(owners, ownerRepository.findAll());
    }

    @Test
    @Order(5)
    void testFindAllPageable() {
        assertEquals(owners, ownerRepository.findAll(pageable).getContent());
    }
}