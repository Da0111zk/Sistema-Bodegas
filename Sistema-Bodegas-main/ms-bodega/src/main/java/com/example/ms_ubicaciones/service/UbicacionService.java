package com.example.ms_ubicaciones.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_ubicaciones.modelo.Ubicacion;
import com.example.ms_ubicaciones.repository.UbicacionRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UbicacionService {
    private final UbicacionRepository ubicacionRepository;

    public List<Ubicacion> obtenerUbicaciones() {
        return ubicacionRepository.findAll();
    }

    public Optional<Ubicacion> obtenerPorId(Long ubicacionId) {
        return ubicacionRepository.findById(ubicacionId);
    }

    public Ubicacion guardar(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    public void eliminar(Long ubicacionId) {
        ubicacionRepository.deleteById(ubicacionId);
    }


}
