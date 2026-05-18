package com.example.ms_stock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_stock.modelo.Stock;
import com.example.ms_stock.service.StockService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> obtenerStocks() {
        return ResponseEntity.ok(stockService.obtenerStocks());
    }

    @GetMapping("/{kardexId}")
    public ResponseEntity<Stock> obtenerPorId(@PathVariable Long kardexId) {
        return stockService.obtenerPorId(kardexId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Stock> guardar(@Valid @RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.guardar(stock));
    }

    @DeleteMapping("/{kardexId}")
    public ResponseEntity<Void> eliminarStock(@PathVariable Long kardexId) {
        stockService.EliminarStock(kardexId);
        return ResponseEntity.noContent().build();
    }

}
