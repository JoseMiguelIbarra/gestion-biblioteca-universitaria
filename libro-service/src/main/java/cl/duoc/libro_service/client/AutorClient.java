package cl.duoc.libro_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.libro_service.dto.AutorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AutorClient {

    @Autowired
    private WebClient autorWebClient;

    public AutorResponse buscarPorId(Long id) {
        log.info("Buscando autor con id: {}", id);
        try {
            return autorWebClient.get()
                    .uri("/autores/{id}", id)
                    .retrieve()
                    .bodyToMono(AutorResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando autor con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró autor con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }
}
