package org.university.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CompanyEmployeeDto {
    private long companyId;
    private String name;
    private long employeeId;
    private String employeeFirstName;
    private String employeeLastName;
}
