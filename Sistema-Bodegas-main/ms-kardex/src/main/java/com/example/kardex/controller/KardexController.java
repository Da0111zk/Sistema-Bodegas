package com.example.kardex.controller;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.service.KardexService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kardex")
@RequiredArgsConstructor
public class KardexController {

    private final KardexService service;

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientos() {
        return ResponseEntity.ok(service.listarMovimientos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> obtenerMovimientoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerMovimientoPorId(id));
    }

    @PostMapping("/movimiento")
    public ResponseEntity<MovimientoResponseDTO> registrarMovimiento(
            @Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarMovimiento(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> actualizarMovimiento(
            @PathVariable Long id,
            @Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizarMovimiento(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMovimiento(@PathVariable Long id) {
        service.eliminarMovimiento(id);
        return ResponseEntity.ok("Registro de kardex eliminado correctamente");
    }

    @GetMapping("/stock/producto/{productoId}/bodega/{bodegaId}")
    public ResponseEntity<StockResponseDTO> obtenerStock(
            @PathVariable Long productoId,
            @PathVariable Long ubicacionId) {
        return ResponseEntity.ok(service.obtenerStock(productoId, ubicacionId));
    }
}