package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity {

    @NotBlank
    @Column(name = "name", unique = true)
    private String name;

    @NotNull
    @PositiveOrZero
    @Column(name = "revenue")
    private BigDecimal revenue = BigDecimal.ZERO;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @ToString.Exclude
    private Set<Vehicle> vehicleSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @ToString.Exclude
    private Set<Employee> employeeSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @ToString.Exclude
    private Set<Transport> transportSet = new HashSet<>();

}
