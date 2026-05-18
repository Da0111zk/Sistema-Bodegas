package com.example.ms_stock.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kardexId;



    @NotNull(message = "La cantidad no puede estar vacía")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int ingresos;


  
    @NotNull(message = "La cantidad no puede estar vacía")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int egresos;

    
    @NotNull(message = "La cantidad no puede estar vacía")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private int  ajustes;

   
    @Column(length = 50)
    private String alerta;
}