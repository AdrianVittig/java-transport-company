package org.university.dto;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CompanyEmployeesDto {
    private long companyId;
    private String name;
    private Set<EmployeeDto> employees;
}
