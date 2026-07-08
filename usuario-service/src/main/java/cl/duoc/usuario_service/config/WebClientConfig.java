package cl.duoc.usuario_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.domicilios.url}")
    private String domicilioServiceUrl;

    @Value("${services.nombres.url}")
    private String nombresServiceUrl;

    @Bean
    public WebClient domicilioWebClient() {
        return WebClient.builder()
                .baseUrl(domicilioServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public WebClient nombreWebClient() {
        return WebClient.builder()
                .baseUrl(nombresServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
