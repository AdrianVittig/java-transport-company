package org.university.dto;

import lombok.*;
import org.university.util.VehicleType;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VehicleDto {

    private Long id;

    private Long companyId;

    private double distanceTraveled;

    private Long employeeId;

    private VehicleType vehicleType;
}
