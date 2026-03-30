package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO{

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @DecimalMin("0.0")
    private BigDecimal balance;


}
