package com.example.kardex.dto;

import lombok.Data;

@Data
public class StockResponseDTO {
    private Long productoId;
    private Long ubicacionId;
    private Integer cantidad;
}