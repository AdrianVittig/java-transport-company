package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.university.validators.ValidateNames;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseEntity {
    @NotBlank
    @ValidateNames
    @Column(name = "name", unique = true)
    private String name;

    @NotNull
    @PositiveOrZero
    @Column(name = "revenue")
    @Builder.Default
    private BigDecimal revenue = BigDecimal.ZERO;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Builder.Default
    private Set<Vehicle> vehicleSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Builder.Default
    private Set<Employee> employeeSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Builder.Default
    private Set<Transport> transportSet = new HashSet<>();

}
