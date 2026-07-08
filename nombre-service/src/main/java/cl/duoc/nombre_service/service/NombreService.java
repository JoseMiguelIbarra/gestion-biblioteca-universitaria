package cl.duoc.nombre_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.nombre_service.dto.NombreRequest;
import cl.duoc.nombre_service.dto.NombreResponse;
import cl.duoc.nombre_service.mapper.NombreMapper;
import cl.duoc.nombre_service.model.Nombre;
import cl.duoc.nombre_service.repository.NombreRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NombreService {

    @Autowired
    private NombreRepository repository;

    @Autowired
    private NombreMapper mapper;

    public List<NombreResponse> obtenerTodos() {
        log.info("Obteniendo nombres...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public NombreResponse buscarPorId(Long id) {
        log.info("Buscando nombre con id: {}", id);
        Nombre nombre = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró nombre con ID: " + id));
        return mapper.toResponse(nombre);
    }

    public NombreResponse crear (NombreRequest request){
        log.info("Creando nombre: {}",request.getNombre());
        Nombre nombre = repository.save(mapper.toEntity(request));
        return mapper.toResponse(nombre);
    }

    public void eliminar (Long id){
        log.info("Eliminando nombre con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Nombre con id" + id + " no encontrado.");
        }
        repository.deleteById(id);
    }

    public NombreResponse actualizar (Long id, NombreRequest request){
        log.info("Actualizando nombre {}", request.getNombre());
        Nombre nombre = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró nombre con id "+ id));

        nombre.setNombre(request.getNombre());
        nombre.setSegundoNombre(request.getSegundoNombre());
        nombre.setApPaterno(request.getApPaterno());
        nombre.setApMaterno(request.getApMaterno());
        

        Nombre nombreActualizado = repository.save(nombre);
        return mapper.toResponse(nombreActualizado);
    }

}
