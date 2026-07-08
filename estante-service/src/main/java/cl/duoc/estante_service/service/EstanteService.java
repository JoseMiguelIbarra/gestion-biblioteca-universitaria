package cl.duoc.estante_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.estante_service.client.BodegaClient;
import cl.duoc.estante_service.dto.BodegaResponse;
import cl.duoc.estante_service.dto.EstanteRequest;
import cl.duoc.estante_service.dto.EstanteResponse;
import cl.duoc.estante_service.mapper.EstanteMapper;
import cl.duoc.estante_service.model.Estante;
import cl.duoc.estante_service.repository.EstanteRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstanteService {

    @Autowired
    private EstanteRepository repository;

    @Autowired
    private BodegaClient bodegaClient;

    @Autowired
    private EstanteMapper mapper;

    public List<EstanteResponse> obtenerTodos() {
        log.info("Consultando estantes...");

        return repository.findAll().stream().map(estante -> {
            BodegaResponse bodega = bodegaClient.buscarPorId(estante.getIdBodega());
            return mapper.toResponse(estante, bodega);
        }).toList();
    }

    public EstanteResponse buscarEstantePorId(Long id) {
        log.info("Buscando estante con id: {}", id);
        return repository.findById(id)
                .map(estante -> {
                    BodegaResponse bodega = bodegaClient.buscarPorId(estante.getIdBodega());
                    return mapper.toResponse(estante, bodega);
                })
                .orElseThrow(() -> new NoSuchElementException("Estante no encontrado con id: " + id));
    }

    public EstanteResponse crearEstante(EstanteRequest request) {
        log.info("Creando nuevo estante: {}", request.getNumero());
        Estante estante = mapper.toEntity(request);

        BodegaResponse bodega = bodegaClient.buscarPorId(request.getIdBodega());
        if (bodega == null) {
            log.warn("Bodega con id: {} no encontrada.", request.getIdBodega());
            throw new NoSuchElementException("Bodega con id " + request.getIdBodega() + " no encontrada");
        }

        return mapper.toResponse(repository.save(estante), bodega);
    }

    public EstanteResponse actualizarEstante(Long id, EstanteRequest request) {
        log.info("Actualizando estante con id: {}", id);

        Estante estanteExistente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró estante con id " + id));

        BodegaResponse bodega = bodegaClient.buscarPorId(request.getIdBodega());
        if (bodega == null) {
            log.warn("Bodega con id: {} no encontrada.", request.getIdBodega());
            throw new NoSuchElementException("Bodega con id " + request.getIdBodega() + " no encontrada");
        }

        estanteExistente.setNumero(request.getNumero());
        estanteExistente.setIdBodega(bodega.getId());
        estanteExistente.setCapacidad(request.getCapacidad());

        Estante estanteActualizado = repository.save(estanteExistente);
        return mapper.toResponse(estanteActualizado, bodega);
    }

    public void eliminarEstante(Long id) {
        log.info("Eliminando estante con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró estante con id: " + id);
        }
        repository.deleteById(id);
    }

    public List<EstanteResponse> buscarEstantePorIdBodega(Long idBodega) {
        log.info("Buscando estantes de bodega con id: {}", idBodega);
        return repository.findByIdBodega(idBodega).stream()
                .map(estante -> {
                    BodegaResponse bodega = bodegaClient.buscarPorId(estante.getIdBodega());
                    return mapper.toResponse(estante, bodega);
                })
                .toList();
    }

}
