package org.university.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.university.util.DrivingLicenseCategories;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "driving_license")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrivingLicense extends BaseEntity {

    @NotBlank
    @Column(name = "driving_license_number", unique = true)
    private String drivingLicenseNumber;

    @NotNull
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @NotNull
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ElementCollection(targetClass = DrivingLicenseCategories.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "driving_license_categories",
            joinColumns = @JoinColumn(name = "driving_license_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Set<DrivingLicenseCategories> drivingLicenseCategories = new HashSet<>();

    @OneToOne(mappedBy = "drivingLicense", fetch = FetchType.LAZY)
    private Employee employee;

    @AssertTrue(message = "Expiry date must be after or equal to issue date")
    private boolean isExpiryAfterIssue() {
        if (issueDate == null || expiryDate == null) return true;
        return !expiryDate.isBefore(issueDate);
    }
}