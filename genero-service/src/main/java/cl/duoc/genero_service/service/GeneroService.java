package cl.duoc.genero_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.mapper.GeneroMapper;
import cl.duoc.genero_service.model.Genero;
import cl.duoc.genero_service.repository.GeneroRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeneroService {

    @Autowired
    private GeneroMapper mapper;

    @Autowired
    private GeneroRepository repository;

    public List<GeneroResponse> obtenerTodos(){
        log.info("Obteniendo géneros...");
        return repository.findAll().stream()
        .map(mapper::toResponse).toList();
    }

    public GeneroResponse buscarPorId(Long id){
        log.info("Buscando el id: {}"+id);
        Genero genero = repository.findById(id).
        orElseThrow(() -> new NoSuchElementException("No genero con id: "+id));
        return mapper.toResponse(genero);
    }

    public GeneroResponse crearGenero(GeneroRequest request){
        log.info("Creando genero: "+ request.getNombre());
        Genero genero = repository.save(mapper.toEntity(request));
        return mapper.toResponse(genero);
    }

    public void eliminarGenero(Long id){
        log.info("Eliminando el id: "+id);
        if (!repository.existsById(id)){
            throw new NoSuchElementException("Genero con id "+id+" no encontrado.");
        }
        repository.deleteById(id);
    }

    public GeneroResponse actualizar (Long id, GeneroRequest request){
        log.info("Actualizando genero con id: "+id);
        Genero genero = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe genero con id: "+id));
        genero.setNombre(request.getNombre());

        Genero generoActualizado = repository.save(genero);
        return mapper.toResponse(generoActualizado);
    }

}
