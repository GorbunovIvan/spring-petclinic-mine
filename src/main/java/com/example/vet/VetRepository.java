package com.example.vet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VetRepository extends JpaRepository<Vet, Integer> {

    @Transactional(readOnly = true)
    @Override
    List<Vet> findAll();
}
