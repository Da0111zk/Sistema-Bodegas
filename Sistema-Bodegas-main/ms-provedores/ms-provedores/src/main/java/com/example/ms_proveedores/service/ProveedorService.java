package com.example.ms_proveedores.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_proveedores.dto.DtoResponseProveedor;
import com.example.ms_proveedores.dto.ProveedoresDto;
import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public List<DtoResponseProveedor> obtenerProveedores() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public Optional<DtoResponseProveedor> obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .map(this::toResponseDto);
    }

    public DtoResponseProveedor guardar(ProveedoresDto proveedorDto) {
        Proveedor proveedor = toEntity(proveedorDto);
        Proveedor guardado = proveedorRepository.save(proveedor);
        return toResponseDto(guardado);
    }

    public void eliminar(Long id) {
        proveedorRepository.deleteById(id);
    }

    private Proveedor toEntity(ProveedoresDto dto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setProveedorId(dto.getProveedorId());
        proveedor.setEstado(dto.getEstado());
        return proveedor;
    }

    private DtoResponseProveedor toResponseDto(Proveedor proveedor) {
        DtoResponseProveedor dto = new DtoResponseProveedor();
        dto.setProveedorId(proveedor.getProveedorId());
        dto.setRazonSocial(proveedor.getRazonSocial());
        dto.setRut(proveedor.getRut());
        dto.setEmail(proveedor.getEmail());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEstado(proveedor.getEstado());
        return dto;
    }
}