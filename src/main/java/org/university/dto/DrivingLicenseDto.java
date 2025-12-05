package org.university.dto;

import lombok.*;
import org.university.entity.Employee;
import org.university.util.Categories;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DrivingLicenseDto {
    private Long id;

    private String drivingLicenseNumber;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private Categories categories;

    private Long employeeId;
}
