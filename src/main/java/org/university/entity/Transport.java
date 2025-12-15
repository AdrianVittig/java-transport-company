package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.university.util.CargoType;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transport")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus")
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.NOT_PAID;

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
    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @AssertTrue(message = "Arrival date must be after or equal to departure date")
    private boolean isArrivalAfterDeparture() {
        if (departureDate == null || arrivalDate == null) return true;
        return !arrivalDate.isBefore(departureDate);
    }
}