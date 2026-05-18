package com.example.ms_stock.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_stock.Repository.StockRepository;
import com.example.ms_stock.modelo.Stock;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository = null;

    public List<Stock> obtenerStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> obtenerPorId(Long kardexId) {
        return stockRepository.findById(kardexId);
    }

        public Stock guardar(Stock stock) {
        return stockRepository.save(stock);
    }



    public void EliminarStock(Long kardexId) {
        stockRepository.deleteById(kardexId);
    }

}
