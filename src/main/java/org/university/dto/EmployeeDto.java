package org.university.dto;

import lombok.*;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Set<DriverQualifications> driverQualifications;
    private Long drivingLicenseId;
    private Long companyId;
    private BigDecimal salary;

}
