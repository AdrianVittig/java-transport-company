package org.university.entity;

import lombok.*;
import org.university.util.VehicleType;

import javax.persistence.*;

@Entity
@Table(name="vehicle")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @Column(name = "distance_traveled")
    private double distanceTraveled;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @Column(name = "vehicle_type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}
