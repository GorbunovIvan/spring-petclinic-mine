package com.example;

import com.example.owner.Owner;
import com.example.owner.Pet;
import com.example.owner.PetType;
import com.example.vet.Specialty;
import com.example.vet.Vet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {

    public static List<Owner> generateOwners() {

        // owners
        Owner owner1 = new Owner();
        owner1.setId(1);
        owner1.setFirstName("test 1 first name");
        owner1.setLastName("test 1 last name");
        owner1.setAddress("test 1 address");
        owner1.setCity("test 1 city");
        owner1.setTelephone("111111111");

        Owner owner2 = new Owner();
        owner2.setId(2);
        owner2.setFirstName("test 2 first name");
        owner2.setLastName("test 2 last name");
        owner2.setAddress("test 2 address");
        owner2.setCity("test 2 city");
        owner2.setTelephone("222222222");

        // pets
        PetType petType1 = new PetType("test 1 petType");
        petType1.setId(1);

        Pet pet1 = new Pet();
        pet1.setId(1);
        pet1.setName("test 1 name");
        pet1.setBirthDate(LocalDate.now());
        pet1.setType(petType1);
        pet1.setOwner(owner1);
        owner1.getPets().add(pet1);

        Pet pet2 = new Pet();
        pet2.setId(2);
        pet2.setName("test 2 name");
        pet2.setBirthDate(LocalDate.now());
        pet2.setType(petType1);
        pet2.setOwner(owner1);
        owner1.getPets().add(pet2);

        Pet pet3 = new Pet();
        pet3.setId(3);
        pet3.setName("test 3 name");
        pet3.setBirthDate(LocalDate.now());
        pet3.setType(petType1);
        pet3.setOwner(owner2);
        owner2.getPets().add(pet3);

        var owners = List.of(owner1, owner2);

        return new ArrayList<>(owners);
    }

    public static List<Vet> generateVets() {

        Specialty specialty1 = new Specialty();
        specialty1.setId(1);
        specialty1.setName("vet");

        Vet vet1 = new Vet();
        vet1.setId(1);
        vet1.setFirstName("vet 1 first name");
        vet1.setLastName("vet 1 last name");
        vet1.setSpecialties(Set.of(specialty1));

        Vet vet2 = new Vet();
        vet2.setId(2);
        vet2.setFirstName("vet 2 first name");
        vet2.setLastName("vet 2 last name");
        vet2.setSpecialties(Set.of(specialty1));

        var vetsList = List.of(vet1, vet2);

        return new ArrayList<>(vetsList);
    }
}
