package com.example.kardex.controller;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.service.KardexService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kardex")
public class KardexController {

    @GetMapping("/")
    public String home() {
        return "ms-kardex funcionando";
    }

    @Autowired
    private KardexService service;

    @PostMapping("/movimiento")
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(@Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarMovimiento(dto));
    }

    @GetMapping("/stock/producto/{productoId}/bodega/{bodegaId}")
    public ResponseEntity<StockResponseDTO> obtenerStock(@PathVariable Long productoId, @PathVariable Long bodegaId) {
        return ResponseEntity.ok(service.obtenerStock(productoId, bodegaId));
    }
}