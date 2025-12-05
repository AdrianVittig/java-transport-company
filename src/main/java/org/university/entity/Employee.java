package org.university.entity;

import lombok.*;
import org.university.util.SpecialQualifications;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="employee")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person{
    @Enumerated(EnumType.STRING)
    @Column(name = "special_qualifications")
    private SpecialQualifications specialQualifications;

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "employee")
    @ToString.Exclude
    private DrivingLicense drivingLicense;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Company company;

    @Column(name = "salary")
    private BigDecimal salary;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Vehicle> vehicleList;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Transport> transportList;
}
