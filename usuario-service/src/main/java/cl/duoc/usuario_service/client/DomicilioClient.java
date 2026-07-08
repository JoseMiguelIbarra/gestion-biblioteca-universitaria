package cl.duoc.usuario_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.usuario_service.dto.DomicilioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DomicilioClient {

    @Autowired
    private WebClient domicilioWebClient;

    public DomicilioResponse buscarDomicilioPorId(Long id) {
        log.info("Buscando domicilio con id: {}", id);
        try {
            return domicilioWebClient.get()
                    .uri("/domicilios/{id}", id)
                    .retrieve()
                    .bodyToMono(DomicilioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando domicilio con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró domicilio con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }
}
