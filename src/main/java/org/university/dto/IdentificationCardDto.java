package org.university.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IdentificationCardDto {
    private Long id;

    private String cardNumber;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private Long personId;
}
