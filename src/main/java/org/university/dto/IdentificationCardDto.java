package org.university.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IdentificationCardDto {
    private Long id;
    private String cardNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Long personId;
}
