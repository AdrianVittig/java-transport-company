package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person {

    @ElementCollection(targetClass = DriverQualifications.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "employee_qualifications",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "qualification")
    private Set<DriverQualifications> driverQualifications = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "driving_license_id")
    private DrivingLicense drivingLicense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @PositiveOrZero
    @Column(name = "salary")
    private BigDecimal salary = BigDecimal.ZERO;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<Vehicle> vehicleSet = new HashSet<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<Transport> transportSet  = new HashSet<>();
}