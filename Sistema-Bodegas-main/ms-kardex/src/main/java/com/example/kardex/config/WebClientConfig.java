package com.example.kardex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${producto.service.url}")
    private String productosUrl;

    @Value("${ubicacion.service.url}")
    private String ubicacionesUrl;

    @Bean
    public WebClient webClientProductos(WebClient.Builder builder) {
        return builder.baseUrl(productosUrl).build();
    }

    @Bean
    public WebClient webClientBodegas(WebClient.Builder builder) {
        return builder.baseUrl(ubicacionesUrl).build();
    }
}