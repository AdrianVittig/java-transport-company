package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.university.util.CargoType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transport")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transport extends BaseEntity {
    @NotBlank
    @Column(name = "start_point")
    private String startPoint;

    @NotBlank
    @Column(name = "end_point")
    private String endPoint;

    @NotNull
    @Column(name = "departure_date")
    private LocalDate departureDate;

    @NotNull
    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type")
    private CargoType cargoType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private Customer customer;

    @Column(name = "paid")
    private boolean paid;

    @NotNull
    @Positive
    @Column(name = "quantity")
    private BigDecimal quantity;

    @NotNull
    @PositiveOrZero
    @Column(name = "init_price")
    private BigDecimal initPrice;

    @NotNull
    @PositiveOrZero
    @Column(name = "price")
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    @ToString.Exclude
    private Vehicle vehicle;
}