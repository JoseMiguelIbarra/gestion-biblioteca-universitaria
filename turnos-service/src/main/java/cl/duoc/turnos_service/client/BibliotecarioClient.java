package cl.duoc.turnos_service.client;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.turnos_service.dto.BibliotecarioResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BibliotecarioClient {

    @Autowired
    private WebClient bibliotecarioWebClient;

    public BibliotecarioResponse buscarPorId(Long id) {
        log.info("Buscando bibliotecario con id: {}", id);
        try {
            return bibliotecarioWebClient.get()
                    .uri("/bibliotecarios/{id}", id)
                    .retrieve()
                    .bodyToMono(BibliotecarioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error buscando el bibliotecario con id {}", id);
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new NoSuchElementException("Bibliotecario no encontrado con id: " + id);
                default -> throw new RuntimeException("Error al consultar el bibliotecario con id: " + id);
            }
        }
    }

}
