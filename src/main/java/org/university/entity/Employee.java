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
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person {

    @ElementCollection(targetClass = DriverQualifications.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "employee_qualifications",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "qualification")
    private Set<DriverQualifications> driverQualifications = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "driving_license_id")
    @ToString.Exclude
    private DrivingLicense drivingLicense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    private Company company;

    @NotNull
    @PositiveOrZero
    @Column(name = "salary")
    private BigDecimal salary = BigDecimal.ZERO;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Vehicle> vehicleSet = new HashSet<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Transport> transportSet  = new HashSet<>();
}