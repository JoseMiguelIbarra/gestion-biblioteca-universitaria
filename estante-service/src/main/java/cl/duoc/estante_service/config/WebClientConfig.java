package cl.duoc.estante_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebClientConfig {

    @Value("${services.bodegas.url}")
    private String bodegaServiceUrl;

    @Bean
    public WebClient bodegaWebClient() {
        return WebClient.builder()
                .baseUrl(bodegaServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

}
