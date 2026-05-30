package com.example.ms_ubicaciones.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_ubicaciones.modelo.Ubicacion;
import com.example.ms_ubicaciones.service.UbicacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ubicaciones")
@RequiredArgsConstructor
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<Object> obtenerUbicaciones() {
        return ResponseEntity.ok(ubicacionService.obtenerUbicaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> obtenerPorId(@PathVariable("id") Long ubicacionId) {
        return ubicacionService.obtenerPorId(ubicacionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ubicacion> guardar(@Valid @RequestBody Ubicacion ubicacion) {
        return ResponseEntity.ok(ubicacionService.guardar(ubicacion));
    }
}