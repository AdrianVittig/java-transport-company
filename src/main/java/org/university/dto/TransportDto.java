package org.university.dto;

import lombok.*;
import org.university.util.CargoType;
import org.university.util.PaymentStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransportDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String startPoint;
    private String endPoint;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private CargoType cargoType;
    private Long companyId;
    private Long employeeId;
    private Long customerId;
    private Long vehicleId;
    private PaymentStatus paymentStatus;
    private BigDecimal quantity;
    private BigDecimal initPrice;
    private BigDecimal totalPrice;
}
