package com.example.owner;

import com.example.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class OwnerRepositoryTest {

    @Autowired
    private OwnerRepository ownerRepository;

    private List<Owner> owners = new ArrayList<>();

    private final Pageable pageable = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {

        var ownersList = Utils.generateOwners();

        // saving to DB
        for (var owner : ownersList) {
            owners.add(ownerRepository.save(owner));
        }
    }

    @Test
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
    void testFindByNameLike() {

        for (var owner : owners) {
            assertEquals(List.of(owner), ownerRepository.findByNameLike(owner.getFirstName(), pageable).getContent());
            assertEquals(List.of(owner), ownerRepository.findByNameLike(owner.getLastName(), pageable).getContent());
        }

        assertEquals(owners, ownerRepository.findByNameLike("test", pageable).getContent());
        assertEquals(Collections.emptyList(), ownerRepository.findByNameLike("none", pageable).getContent());

    }

    @Test
    void testFindById() {

        for (var owner : owners) {
            var ownerOpt = ownerRepository.findById(owner.getId());
            assertTrue(ownerOpt.isPresent());
            assertEquals(owner, ownerOpt.get());
        }

        assertTrue(ownerRepository.findById(-1).isEmpty());
    }

    @Test
    void testFindAll() {
        assertEquals(owners, ownerRepository.findAll());
    }

    @Test
    void testFindAllPageable() {
        assertEquals(owners, ownerRepository.findAll(pageable).getContent());
    }
}