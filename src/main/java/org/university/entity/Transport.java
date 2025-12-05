package org.university.entity;

import lombok.*;
import org.university.util.CargoType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="transport")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy=InheritanceType.JOINED)
public class Transport extends BaseEntity {
    @Column(name = "start_point")
    private String startPoint;

    @Column(name = "end_point")
    private String endPoint;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type")
    private CargoType cargoType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column (name="init_price")
    private BigDecimal initPrice;

    @Column(name = "price")
    private BigDecimal totalPrice;


}
