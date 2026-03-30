package com.epam.rd.autocode.spring.project.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee extends User{

    @NotBlank
    @Column(unique = true)
    private String phone;

    @NotNull
    private LocalDate birthDate;

    public Employee(Long id, String email, String name, String password, String phone, LocalDate birthDate) {
        super(id, email, name, password);
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
