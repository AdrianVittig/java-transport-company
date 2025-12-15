package org.university.dto;

import lombok.*;
import org.university.util.DrivingLicenseCategories;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrivingLicenseDto {
    private Long id;
    private String drivingLicenseNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Set<DrivingLicenseCategories> drivingLicenseCategories;
    private Long employeeId;
}
