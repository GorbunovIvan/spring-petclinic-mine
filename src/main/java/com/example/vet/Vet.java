package com.example.vet;

import com.example.model.Person;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "vets")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString(callSuper = true)
public class Vet extends Person {

    @ManyToMany
    @JoinTable(name = "vet_specialties",
                joinColumns = { @JoinColumn(name = "vet_id") },
                inverseJoinColumns = { @JoinColumn(name = "specialty_id") }
    )
    @ToString.Exclude
    private Set<Specialty> specialties;
}
