package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.university.util.VehicleType;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @NotNull
    @PositiveOrZero
    @Column(name = "distance_traveled")
    private BigDecimal distanceTraveled = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    private Company company;
}
