package com.example.owner;

import com.example.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "types")
@AllArgsConstructor
public class PetType extends NamedEntity {

    public PetType(String name) {
        setName(name);
    }
}
