package org.university.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="company")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {
    @Column(name="name")
    private String name;

    @Column(name="revenue")
    private BigDecimal revenue;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "company")
    @ToString.Exclude
    private List<Vehicle> vehicleList;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "company")
    @ToString.Exclude
    private List<Employee> employeeList;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true,
            mappedBy = "company")
    @ToString.Exclude
    private List<Transport> transportList;
}
