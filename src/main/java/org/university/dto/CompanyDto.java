package org.university.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyDto {
    private Long id;

    private String name;

    private BigDecimal revenue;

    private List<Long> vehicleIds;

    private List<Long> employeeIds;

    private List<Long> transportIds;
}
