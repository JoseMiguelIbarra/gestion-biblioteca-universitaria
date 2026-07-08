package cl.duoc.libro_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.libro_service.dto.GeneroResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GeneroClient {

    @Autowired
    private WebClient generoWebClient;

    public GeneroResponse buscarPorId(Long id) {
        log.info("Buscando genero con id: {}", id);
        try {
            return generoWebClient.get()
                    .uri("/generos/{id}", id)
                    .retrieve()
                    .bodyToMono(GeneroResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando genero con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró genero con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }

}
