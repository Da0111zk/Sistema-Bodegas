package com.example.ms_stock.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_stock.modelo.Stock;
public interface StockRepository extends JpaRepository<Stock, Long> {

}
