package cl.duoc.estante_service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.estante_service.dto.BodegaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BodegaClient {

    @Autowired
    private WebClient bodegaWebClient;

    public BodegaResponse buscarPorId(Long id) {
        log.info("Buscando bodega con id: {}", id);
        try {
            return bodegaWebClient.get()
                    .uri("/bodegas/{id}", id)
                    .retrieve()
                    .bodyToMono(BodegaResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando bodega con id {}", id, ex);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("No se encontró bodega con id " + id);
                default -> throw new RuntimeException("Error buscando el id " + id, ex);
            }
        }
    }

}
