
package com.example.ms_stock.webcontroller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.ms_categorias.modelo.Categoria;
import com.example.ms_categorias.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StockRepository stockRepository;

    @Override
    public void run(String... args) {
        if (stockRepository.count() > 0) {
            log.info(">> Stocks ya existen en la BD, omitiendo carga.");
            return;
        }
        
        log.info(">> Iniciando carga de stocks de prueba...");

        stockRepository.save(new Stock(null, 23, 100, 12,3));
        stockRepository.save(new Stock(null, 24, 200, 15,5));
        stockRepository.save(new Stock(null, 25, 300, 18,7));
        
        log.info(">> Carga inicial finalizada con éxito.");
    }

}
