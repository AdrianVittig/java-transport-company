package org.university.dto;

import lombok.*;
import org.university.util.VehicleType;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private VehicleType vehicleType;
    private BigDecimal distanceTraveled;
    private Long employeeId;
    private Long companyId;
}
