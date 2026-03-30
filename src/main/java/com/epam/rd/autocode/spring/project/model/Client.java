package com.epam.rd.autocode.spring.project.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;


import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "clients")
public class Client extends User {

    @DecimalMin("0.0")
    private BigDecimal balance;

    public Client(Long id, String email, String name, String password, BigDecimal balance) {
        super(id, email, name, password);
        this.balance = balance;
    }
}
