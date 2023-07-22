package com.example.owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    @Query("FROM PetType pType ORDER BY pType.name")
    @Transactional(readOnly = true)
    List<PetType> findPetTypes();

    @Transactional(readOnly = true)
    List<Owner> findByLastNameStartingWith(String lastName);

    @Query("FROM Owner owner LEFT JOIN FETCH owner.pets WHERE owner.id = :id")
    @Transactional(readOnly = true)
    @Override
    Optional<Owner> findById(Integer id);

    @Transactional(readOnly = true)
    @Override
    List<Owner> findAll();
}
