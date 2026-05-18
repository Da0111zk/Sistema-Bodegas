package com.example.ms_stock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestDTO {
    @NotNull(message = "El ID del kardex es obligatorio")
    private Long kardexId;

    @NotNull(message = "Los ingresos son obligatorios")
    private int ingresos;

    @NotNull(message = "Los egresos son obligatorios")
    private int egresos;
    

    @NotNull(message = "Los ajustes son obligatorios")
    private int ajustes;

    @NotBlank(message = "La alerta no puede estar vacía")    
    private String alerta;



}
