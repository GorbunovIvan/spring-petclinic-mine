package com.example.owner;

import com.example.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "visits")
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Visit extends BaseEntity {

    @Column(name = "visit_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "description")
    @NotEmpty
    private String description;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public Visit() {
        this.date = LocalDate.now();
    }
}
