package com.franchise.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarStockRequest {

    @NotNull(message = "El stock es requerido")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private Integer stock;
}