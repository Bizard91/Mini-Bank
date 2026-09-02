package com.example.minibank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull(message = "Client id is required")
    private Long clientId;

    @NotBlank(message = "Currency is required")
    private String currency;
}