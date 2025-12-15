package org.university.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Person {
    @NotNull
    @PositiveOrZero
    @Column(name = "budget")
    private BigDecimal budget = BigDecimal.ZERO;

    @OneToMany(mappedBy = "customer")
    private Set<Transport> transportSet = new HashSet<>();
}
