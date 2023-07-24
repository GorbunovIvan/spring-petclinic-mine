package com.example.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {

    @Query("FROM PetType pType ORDER BY pType.name")
    @Transactional(readOnly = true)
    List<PetType> findPetTypes();

    @Query("FROM Owner WHERE firstName LIKE :name% OR lastName LIKE :name% ORDER BY lastName, firstName")
    @Transactional(readOnly = true)
    Page<Owner> findByNameLike(String name, Pageable pageable);

    @Query("FROM Owner owner " +
            "LEFT JOIN FETCH owner.pets pets " +
            "LEFT JOIN FETCH pets.visits " +
            "LEFT JOIN FETCH pets.type type " +
            "WHERE owner.id = :id")
    @Transactional(readOnly = true)
    @Override
    Optional<Owner> findById(Integer id);

    @Query("FROM Owner ORDER BY lastName, firstName")
    @Transactional(readOnly = true)
    @Override
    List<Owner> findAll();

    @Query("FROM Owner ORDER BY lastName, firstName")
    @Transactional(readOnly = true)
    @Override
    Page<Owner> findAll(Pageable pageable);
}
