package org.university.dto;

import lombok.*;
import org.university.util.CargoType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
    private Long vehicleId;
    private boolean paid;
    private BigDecimal quantity;
    private BigDecimal initPrice;
    private BigDecimal totalPrice;
}
