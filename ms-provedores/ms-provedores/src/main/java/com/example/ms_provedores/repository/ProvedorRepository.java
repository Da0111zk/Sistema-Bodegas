package com.example.ms_provedores.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_provedores.modelo.Provedor;

public interface ProvedorRepository extends JpaRepository<Provedor, Long> {
    
}