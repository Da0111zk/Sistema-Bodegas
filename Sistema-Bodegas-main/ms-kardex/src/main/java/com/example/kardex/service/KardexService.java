package com.example.kardex.service;

import com.example.kardex.dto.MovimientoRequestDTO;
import com.example.kardex.dto.MovimientoResponseDTO;
import com.example.kardex.dto.StockResponseDTO;
import com.example.kardex.model.MovimientoKardex;
import com.example.kardex.model.StockActual;
import com.example.kardex.repository.MovimientoKardexRepository;
import com.example.kardex.repository.StockActualRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class KardexService {

    private final MovimientoKardexRepository movimientoRepository;
    private final StockActualRepository stockRepository;
    private final WebClient webClientProductos;
    private final WebClient webClientBodegas;

    public KardexService(
            MovimientoKardexRepository movimientoRepository,
            StockActualRepository stockRepository,
            @Qualifier("webClientProductos") WebClient webClientProductos,
            @Qualifier("webClientBodegas") WebClient webClientBodegas
    ) {
        this.movimientoRepository = movimientoRepository;
        this.stockRepository = stockRepository;
        this.webClientProductos = webClientProductos;
        this.webClientBodegas = webClientBodegas;
    }

    @Transactional
    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO dto) {
        validarProductoYBodega(dto.getProductoId(), dto.getUbicacionId());
        validarReglasMovimiento(dto);

        StockActual stock = stockRepository.findByProductoIdAndUbicacionId(
                        dto.getProductoId(), dto.getUbicacionId())
                .orElseThrow(() -> new RuntimeException(
                        "No existe configuración de stock para el producto " + dto.getProductoId() +
                        " en la bodega " + dto.getUbicacionId()
                ));

        int stockAnterior = stock.getCantidad();
        String signoNormalizado = normalizarSigno(dto);
        int stockNuevo = calcularStockNuevo(stockAnterior, dto, signoNormalizado);

        stock.setCantidad(stockNuevo);
        stockRepository.save(stock);

        MovimientoKardex movimiento = new MovimientoKardex();
        movimiento.setProductoId(dto.getProductoId());
        movimiento.setUbicacionId(dto.getUbicacionId());
        movimiento.setTipoMovimiento(dto.getTipoMovimiento());
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setSigno(signoNormalizado);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setReferencia(dto.getReferencia());

        movimiento = movimientoRepository.save(movimiento);
        return toResponse(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> listarMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimientoResponseDTO obtenerMovimientoPorId(Long id) {
        MovimientoKardex movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));

        return toResponse(movimiento);
    }

    @Transactional
    public MovimientoResponseDTO actualizarMovimiento(Long id, MovimientoRequestDTO dto) {
        validarProductoYBodega(dto.getProductoId(), dto.getUbicacionId());
        validarReglasMovimiento(dto);

        MovimientoKardex movimientoExistente = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));

        StockActual stockOriginal = stockRepository
                .findByProductoIdAndUbicacionId(
                        movimientoExistente.getProductoId(),
                        movimientoExistente.getUbicacionId()
                )
                .orElseThrow(() -> new RuntimeException("No existe stock asociado al movimiento original"));

        revertirMovimiento(stockOriginal, movimientoExistente);
        stockRepository.save(stockOriginal);

        StockActual stockNuevoContexto = stockRepository
                .findByProductoIdAndUbicacionId(dto.getProductoId(), dto.getUbicacionId())
                .orElseThrow(() -> new RuntimeException(
                        "No existe configuración de stock para el producto " + dto.getProductoId() +
                        " en la bodega " + dto.getUbicacionId()
                ));

        int nuevoStockAnterior = stockNuevoContexto.getCantidad();
        String signoNormalizado = normalizarSigno(dto);
        int nuevoStockCalculado = calcularStockNuevo(nuevoStockAnterior, dto, signoNormalizado);

        stockNuevoContexto.setCantidad(nuevoStockCalculado);
        stockRepository.save(stockNuevoContexto);

        movimientoExistente.setProductoId(dto.getProductoId());
        movimientoExistente.setUbicacionId(dto.getUbicacionId());
        movimientoExistente.setTipoMovimiento(dto.getTipoMovimiento());
        movimientoExistente.setCantidad(dto.getCantidad());
        movimientoExistente.setSigno(signoNormalizado);
        movimientoExistente.setStockAnterior(nuevoStockAnterior);
        movimientoExistente.setStockNuevo(nuevoStockCalculado);
        movimientoExistente.setFechaMovimiento(LocalDateTime.now());
        movimientoExistente.setReferencia(dto.getReferencia());

        movimientoExistente = movimientoRepository.save(movimientoExistente);
        return toResponse(movimientoExistente);
    }

    @Transactional
    public void eliminarMovimiento(Long id) {
        MovimientoKardex movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));

        StockActual stock = stockRepository
                .findByProductoIdAndUbicacionId(movimiento.getProductoId(), movimiento.getUbicacionId())
                .orElseThrow(() -> new RuntimeException("No existe stock asociado al movimiento"));

        revertirMovimiento(stock, movimiento);
        stockRepository.save(stock);

        movimientoRepository.delete(movimiento);
    }

    @Transactional(readOnly = true)
    public StockResponseDTO obtenerStock(Long productoId, Long ubicacionId) {
        validarProductoYBodega(productoId, ubicacionId);

        StockActual stock = stockRepository.findByProductoIdAndUbicacionId(productoId, ubicacionId)
                .orElseThrow(() -> new RuntimeException("No existe stock para ese producto y bodega"));

        StockResponseDTO dto = new StockResponseDTO();
        dto.setProductoId(stock.getProductoId());
        dto.setUbicacionId(stock.getUbicacionId());
        dto.setCantidad(stock.getCantidad());
        return dto;
    }

    private void validarProductoYBodega(Long productoId, Long ubicacionId) {
        validarProducto(productoId);
        validarBodega(ubicacionId);
    }

    private void validarProducto(Long productoId) {
        try {
            webClientProductos.get()
                    .uri("/{id}", productoId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new RuntimeException("El producto con id " + productoId + " no existe");
        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Error al validar el producto con id " + productoId +
                    ". HTTP Status: " + ex.getStatusCode().value()
            );
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error técnico al validar el producto con id " + productoId +
                    ": " + ex.getMessage()
            );
        }
    }

    private void validarBodega(Long ubicacionId) {
        try {
            webClientBodegas.get()
                    .uri("/{id}", ubicacionId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            throw new RuntimeException("La bodega con id " + ubicacionId + " no existe");
        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Error al validar la bodega con id " + ubicacionId +
                    ". HTTP Status: " + ex.getStatusCode().value()
            );
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error técnico al validar la bodega con id " + ubicacionId +
                    ": " + ex.getMessage()
            );
        }
    }

    private void validarReglasMovimiento(MovimientoRequestDTO dto) {
        if ("AJUSTE".equals(dto.getTipoMovimiento())) {
            if (dto.getSigno() != null) {
                String signo = dto.getSigno().trim().toUpperCase();
                if (!"POSITIVO".equals(signo) && !"NEGATIVO".equals(signo)) {
                    throw new RuntimeException("Para AJUSTE el signo debe ser POSITIVO o NEGATIVO");
                }
            }
        } else {
            if (dto.getSigno() != null && !dto.getSigno().trim().isEmpty()) {
                throw new RuntimeException("El signo solo aplica para movimientos de tipo AJUSTE");
            }
        }
    }

    private String normalizarSigno(MovimientoRequestDTO dto) {
        if (!"AJUSTE".equals(dto.getTipoMovimiento())) {
            return null;
        }

        if (dto.getSigno() == null || dto.getSigno().trim().isEmpty()) {
            return "POSITIVO";
        }

        return dto.getSigno().trim().toUpperCase();
    }

    private int calcularStockNuevo(int stockAnterior, MovimientoRequestDTO dto, String signoNormalizado) {
        return switch (dto.getTipoMovimiento()) {
            case "INGRESO" -> stockAnterior + dto.getCantidad();

            case "EGRESO" -> {
                int stockNuevo = stockAnterior - dto.getCantidad();
                if (stockNuevo < 0) {
                    throw new RuntimeException("Stock insuficiente para realizar el egreso");
                }
                yield stockNuevo;
            }

            case "AJUSTE" -> {
                int stockNuevo = "NEGATIVO".equals(signoNormalizado)
                        ? stockAnterior - dto.getCantidad()
                        : stockAnterior + dto.getCantidad();

                if (stockNuevo < 0) {
                    throw new RuntimeException("El ajuste dejaría stock negativo");
                }
                yield stockNuevo;
            }

            default -> throw new RuntimeException("Tipo de movimiento inválido");
        };
    }

    private void revertirMovimiento(StockActual stock, MovimientoKardex movimiento) {
        int cantidadActual = stock.getCantidad();
        int cantidadRevertida;

        switch (movimiento.getTipoMovimiento()) {
            case "INGRESO" -> cantidadRevertida = cantidadActual - movimiento.getCantidad();

            case "EGRESO" -> cantidadRevertida = cantidadActual + movimiento.getCantidad();

            case "AJUSTE" -> {
                String signo = movimiento.getSigno() == null ? "POSITIVO" : movimiento.getSigno();
                cantidadRevertida = "NEGATIVO".equals(signo)
                        ? cantidadActual + movimiento.getCantidad()
                        : cantidadActual - movimiento.getCantidad();
            }

            default -> throw new RuntimeException("Tipo de movimiento inválido al revertir");
        }

        if (cantidadRevertida < 0) {
            throw new RuntimeException("La reversa del movimiento dejaría stock negativo");
        }

        stock.setCantidad(cantidadRevertida);
    }

    private MovimientoResponseDTO toResponse(MovimientoKardex movimiento) {
        MovimientoResponseDTO dto = new MovimientoResponseDTO();
        dto.setId(movimiento.getId());
        dto.setProductoId(movimiento.getProductoId());
        dto.setUbicacionId(movimiento.getUbicacionId());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setCantidad(movimiento.getCantidad());
        dto.setSigno(movimiento.getSigno());
        dto.setStockAnterior(movimiento.getStockAnterior());
        dto.setStockNuevo(movimiento.getStockNuevo());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        dto.setReferencia(movimiento.getReferencia());
        return dto;
    }
}