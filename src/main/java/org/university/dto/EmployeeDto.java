package org.university.dto;
import lombok.*;
import org.university.util.SpecialQualifications;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeDto {
    private Long id;

    private String firstName;

    private String lastName;

    private SpecialQualifications specialQualifications;

    private Long drivingLicenseId;

    private Long companyId;

    private BigDecimal salary;

    private List<Long> vehicleIds;

    private List<Long> transportIds;
}
