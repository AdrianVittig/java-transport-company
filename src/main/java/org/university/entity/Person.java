package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.university.validators.ValidateNames;

import java.time.LocalDate;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Builder
public class Person extends BaseEntity {
    @NotBlank
    @Column(name = "first_name")
    @ValidateNames
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    @ValidateNames
    private String lastName;

    @NotNull
    @PastOrPresent
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "person")
    private IdentificationCard identificationCard;
}