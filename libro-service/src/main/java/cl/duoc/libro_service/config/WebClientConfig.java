package cl.duoc.libro_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.autores.url}")
    private String autorServiceUrl;

    @Value("${services.genero.url}")
    private String generoServiceUrl;

    @Bean
    public WebClient autorWebClient() {
        return WebClient.builder()
                .baseUrl(autorServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public WebClient generoWebClient() {
        return WebClient.builder()
                .baseUrl(generoServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
