package cl.duoc.autor_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.mapper.AutorMapper;
import cl.duoc.autor_service.model.Autor;
import cl.duoc.autor_service.repository.AutorRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AutorService {

    @Autowired
    private AutorRepository repository;

    @Autowired
    private AutorMapper mapper;

    public List<AutorResponse> obtenerTodos() {
        log.info("Obteniendo autores...");
        return repository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    public AutorResponse buscarPorId(Long id) {
        log.info("Buscando autor con id: {}", id);
        Autor autor = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró autor con ID: " + id));
        return mapper.toResponse(autor);
    }

    public AutorResponse crear (AutorRequest request){
        log.info("Creando autor: {}",request.getNombre());
        Autor autor = repository.save(mapper.toEntity(request));
        return mapper.toResponse(autor);
    }

    public void eliminar (Long id){
        log.info("Eliminando autor con id: {}", id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Autor con id" + id + " no encontrado(a).");
        }
        repository.deleteById(id);
    }

    public AutorResponse actualizar (Long id, AutorRequest request){
        log.info("Actualizando autor {}", request.getNombre());
        Autor autor = repository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("No se encontró autor con id "+ id));

        autor.setNombre(request.getNombre());
        autor.setFecha_nac(request.getFecha_nac());

        Autor autorActualizado = repository.save(autor);
        return mapper.toResponse(autorActualizado);
    }

}
