package com.example.owner;

import com.example.model.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owners")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Owner extends Person {

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "city")
    @NotEmpty
    private String city;

    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    private String telephone;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @OrderBy("name")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Pet> pets = new ArrayList<>();

    public Pet getPet(Pet pet) {

        if (pet.getName().isEmpty()) {
            return null;
        }

        return this.getPets().stream()
                .filter(p -> p.getName().equals(pet.getName()))
                .findAny()
                .orElse(null);
    }

    public Pet getPet(Integer petId) {

        if (petId == null) {
            return null;
        }

        return this.getPets().stream()
                .filter(p -> p.getId().equals(petId))
                .findAny()
                .orElse(null);
    }

    public void addPet(Pet pet) {
        if (pet.isNew()) {
            getPets().add(pet);
        }
    }

    public void addVisit(Integer petId, Visit visit) {
        Pet pet = getPet(petId);
        pet.addVisit(visit);
    }
}
