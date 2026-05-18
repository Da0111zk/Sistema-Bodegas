package com.example.ms_stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoResponseStock {
    private Long stockId;
    private Long kardexId;
    private int ingresos;
    private int egresos;
    private int ajustes;
    private String alerta;

}
