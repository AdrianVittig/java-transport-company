package org.university.entity;

import lombok.*;
import org.university.util.Categories;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="driving_license")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DrivingLicense extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    private Employee employee;

    @Column(name = "driving_license_number")
    private String drivingLicenseNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "categories")
    @Enumerated(EnumType.STRING)
    private Categories categories;
}
