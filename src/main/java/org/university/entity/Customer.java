package org.university.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="client")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Person{
    @Column(name = "budget")
    private BigDecimal budget;

    @OneToMany(mappedBy = "customer")
    private List<Transport> transportList;
}
