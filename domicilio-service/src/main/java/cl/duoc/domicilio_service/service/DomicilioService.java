package cl.duoc.domicilio_service.service;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.domicilio_service.dto.DomicilioRequest;
import cl.duoc.domicilio_service.dto.DomicilioResponse;
import cl.duoc.domicilio_service.mapper.DomicilioMapper;
import cl.duoc.domicilio_service.model.Domicilio;
import cl.duoc.domicilio_service.repository.DomicilioRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DomicilioService {

    @Autowired
    private DomicilioRepository repository;

    @Autowired
    private DomicilioMapper mapper;

    public List<DomicilioResponse> obtenerTodos() {
        log.info("Obteniendo todos los domicilios...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public DomicilioResponse buscarPorId(Long id) {
        log.info("Buscando domicilio por id: {}", id);
        Domicilio dom = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró domicilio con ID: " + id));
        return mapper.toResponse(dom);
    }

    public DomicilioResponse crear (DomicilioRequest request){
        log.info("Creando domicilio: {}",request.getDireccion());
        Domicilio dom = repository.save(mapper.toEntity(request));
        return mapper.toResponse(dom);
    }

    public void eliminar (Long id){
        log.info("Eliminando domicilio con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Domicilio con id" + id + " no encontrado.");
        }
        repository.deleteById(id);
    }

    public DomicilioResponse actualizar (Long id, DomicilioRequest request){
        log.info("Actualizando domicilio {}", request.getDireccion());
        Domicilio dom = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró domicilio con id "+ id));

        dom.setDireccion(request.getDireccion());
        dom.setComuna(request.getComuna());
        dom.setPais(request.getPais());

        Domicilio domicilioActualizado = repository.save(dom);
        return mapper.toResponse(domicilioActualizado);
    }

}