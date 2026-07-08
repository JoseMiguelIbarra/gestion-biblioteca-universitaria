package cl.duoc.bodega_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.bodega_service.dto.BodegaRequest;
import cl.duoc.bodega_service.dto.BodegaResponse;
import cl.duoc.bodega_service.mapper.BodegaMapper;
import cl.duoc.bodega_service.model.Bodega;
import cl.duoc.bodega_service.repository.BodegaRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BodegaService {
    @Autowired
    private BodegaRepository repository;

    @Autowired
    private BodegaMapper mapper;

    public List<BodegaResponse> obtenerTodas() {
        log.info("Obteniendo bodegas...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public BodegaResponse buscarBodegaPorId(Long id) {
        log.info("Buscando bodega con id: {}", id);
        Bodega bodega = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró bodega con ID: " + id));
        return mapper.toResponse(bodega);
    }

    public BodegaResponse crear (BodegaRequest request){
        log.info("Creando bodega: {}",request.getNombre());
        Bodega bodega = repository.save(mapper.toEntity(request));
        return mapper.toResponse(bodega);
    }

    public void eliminar (Long id){
        log.info("Eliminando bodega con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Bodega con id" + id + " no encontrada.");
        }
        repository.deleteById(id);
    }

    public BodegaResponse actualizar (Long id, BodegaRequest request){
        log.info("Actualizando autor {}", request.getNombre());
        Bodega bodega = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró bodega con id "+ id));

        bodega.setNombre(request.getNombre());
        bodega.setUbicacion(request.getUbicacion());
        bodega.setCapacidad(request.getCapacidad());

        Bodega bodegaActualizada = repository.save(bodega);
        return mapper.toResponse(bodegaActualizada);
    }
}
