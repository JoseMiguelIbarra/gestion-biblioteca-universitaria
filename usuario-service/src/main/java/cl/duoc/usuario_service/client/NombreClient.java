package cl.duoc.usuario_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.usuario_service.dto.NombreResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NombreClient {

    @Autowired
    private WebClient nombreWebClient;

    public NombreResponse buscarNombrePorId(Long id) {
        log.info("Buscando nombre con id: {}", id);
        try {
            return nombreWebClient.get()
                    .uri("/nombres/{id}", id)
                    .retrieve()
                    .bodyToMono(NombreResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando nombre con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró nombre con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }
}
