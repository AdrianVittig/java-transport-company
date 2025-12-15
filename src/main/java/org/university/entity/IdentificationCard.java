package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "identification_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentificationCard extends BaseEntity {
    @NotBlank
    @Column(name = "card_number", unique = true)
    private String cardNumber;

    @NotNull
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @NotNull
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "person_id", unique = true)
    private Person person;
}