package cl.duoc.turnos_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.bibliotecarios.url}")
    private String bibliotecariosUrl;

    @Bean
    public WebClient bibliotecarioWebClient() {
        return WebClient.builder()
                .baseUrl(bibliotecariosUrl)
                .build();
    }

}
