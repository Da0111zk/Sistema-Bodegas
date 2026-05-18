package com.example.ms_provedores.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoResponseProvedor {
    
    private Long provedorId;
    private String razonSocial;
    private int rut;
    private String email;
    private int telefono;
    private String estado;

}
