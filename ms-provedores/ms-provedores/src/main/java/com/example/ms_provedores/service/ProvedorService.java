package com.example.ms_provedores.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_provedores.modelo.Provedor;
import com.example.ms_provedores.repository.ProvedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProvedorService {
    private final ProvedorRepository provedorRepository;

    public List<Provedor> obtenerProvedores() {
        return provedorRepository.findAll();
    }

    public Optional<Provedor> obtenerPorId(Long id) {
        return provedorRepository.findById(id);
    }

    public Provedor guardar(Provedor  provedor) {
        return provedorRepository.save(provedor);
    }

    public void eliminar(Long id) {
        provedorRepository.deleteById(id);
    }
}