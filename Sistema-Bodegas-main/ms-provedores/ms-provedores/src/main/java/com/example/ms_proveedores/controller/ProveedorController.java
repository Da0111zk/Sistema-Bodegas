package com.example.ms_proveedores.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_proveedores.dto.DtoResponseProveedor;
import com.example.ms_proveedores.dto.ProveedoresDto;
import com.example.ms_proveedores.service.ProveedorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/proveedor")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<DtoResponseProveedor>> obtenerProveedores() {
        return ResponseEntity.ok(proveedorService.obtenerProveedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoResponseProveedor> obtenerPorId(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DtoResponseProveedor> crear(@Valid @RequestBody ProveedoresDto proveedorDto) {
        DtoResponseProveedor nuevo = proveedorService.guardar(proveedorDto);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DtoResponseProveedor> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody ProveedoresDto datos) {
        return proveedorService.obtenerPorId(id)
                .map(existente -> {
                    datos.setProveedorId(id);
                    return ResponseEntity.ok(proveedorService.guardar(datos));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (proveedorService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}