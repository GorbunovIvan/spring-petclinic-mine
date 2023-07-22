package com.example.vet;

import com.example.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "specialties")
@AllArgsConstructor
@Getter @Setter
public class Specialty extends NamedEntity {
}
