package cl.duoc.bibliotecario_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.mapper.BibliotecarioMapper;
import cl.duoc.bibliotecario_service.model.Bibliotecario;
import cl.duoc.bibliotecario_service.repository.BibliotecarioRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BibliotecarioService {

    @Autowired
    private BibliotecarioRepository repository;

    @Autowired
    private BibliotecarioMapper mapper;

    public List<BibliotecarioResponse> obtenerTodos() {
        log.info("Consultando bibliotecarios...");
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BibliotecarioResponse buscarPorId(Long id) {
        log.info("Buscando bibliotecario con id: {}", id);
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException(
                    "No se encontró bibliotecario con id: " + id));
    }

    public BibliotecarioResponse crear(BibliotecarioRequest request) {
        log.info("Creando nuevo bibliotecario");
        Bibliotecario guardado = repository.save(mapper.toEntity(request));
        return mapper.toResponse(guardado);
    }

    public BibliotecarioResponse actualizar(Long id, BibliotecarioRequest request) {
        log.info("Actualizando bibliotecario con id: {}", id);
        Bibliotecario existente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                    "No se encontró bibliotecario con id: " + id));

        existente.setNombre(request.getNombre());
        existente.setEdad(request.getEdad());
        existente.setFechaIngreso(request.getFechaIngreso());

        return mapper.toResponse(repository.save(existente));
    }

    public void eliminar(Long id) {
        log.info("Eliminando bibliotecario con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException(
                "No se encontró bibliotecario con id: " + id);
        }
        repository.deleteById(id);
    }
}