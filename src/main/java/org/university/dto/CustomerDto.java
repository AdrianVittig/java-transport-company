package org.university.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomerDto {
    private Long id;

    private String firstName;

    private String lastName;

    private BigDecimal budget;

    private List<Long> transportIds;
}
