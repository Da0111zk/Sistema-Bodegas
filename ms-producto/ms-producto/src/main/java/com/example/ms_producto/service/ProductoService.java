package com.example.ms_producto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_producto.modelo.Producto;
import com.example.ms_producto.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long productoId) {
        return productoRepository.findById(productoId);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Long productoId) {
        productoRepository.deleteById(productoId);
    }
}