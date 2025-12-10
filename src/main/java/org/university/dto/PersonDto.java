package org.university.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Long identificationCardId;
}
