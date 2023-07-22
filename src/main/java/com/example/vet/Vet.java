package com.example.vet;

import com.example.model.Person;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "vets")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
public class Vet extends Person {

    @ManyToMany
    @JoinTable(name = "vet_specialties",
                joinColumns = { @JoinColumn(name = "specialty_id") },
                inverseJoinColumns = { @JoinColumn(name = "vet_id") }
    )
    private Set<Specialty> specialties;
}
