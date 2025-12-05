package org.university.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="identification_card")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IdentificationCard extends BaseEntity {
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private Person person;
}
