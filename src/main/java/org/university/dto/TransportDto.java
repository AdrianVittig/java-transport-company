package org.university.dto;

import lombok.*;
import org.university.util.CargoType;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransportDto {
    private Long id;

    private String startPoint;

    private String endPoint;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private CargoType cargoType;

    private Long companyId;

    private Long employeeId;

    private Long customerId;

    private boolean isPaid;

    private BigDecimal quantity;

    private BigDecimal initPrice;

    private BigDecimal totalPrice;
}
