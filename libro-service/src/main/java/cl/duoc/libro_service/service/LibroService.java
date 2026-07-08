package cl.duoc.libro_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.libro_service.client.AutorClient;
import cl.duoc.libro_service.client.GeneroClient;
import cl.duoc.libro_service.dto.AutorResponse;
import cl.duoc.libro_service.dto.GeneroResponse;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.mapper.LibroMapper;
import cl.duoc.libro_service.model.Libro;
import cl.duoc.libro_service.repository.LibroRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibroService {

    @Autowired
    private LibroRepository repository;

    @Autowired
    private GeneroClient generoClient;

    @Autowired
    private AutorClient autorClient;

    @Autowired
    private LibroMapper mapper;

    public List<LibroResponse> obtenerTodos() {
        log.info("Consultando libros...");

        return repository.findAll().stream().map(libro -> {
            AutorResponse autor = autorClient.buscarPorId(libro.getIdAutor());
            GeneroResponse genero = generoClient.buscarPorId(libro.getIdGenero());
            return mapper.toResponse(libro, genero, autor);
        }).toList();
    }

    public LibroResponse buscarPorId(Long id) {
        log.info("Buscando libro con id: {}", id);
        return repository.findById(id)
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarPorId(libro.getIdAutor());
                    GeneroResponse genero = generoClient.buscarPorId(libro.getIdGenero());
                    return mapper.toResponse(libro, genero, autor);
                })
                .orElseThrow(() -> new NoSuchElementException("Libro no encontrado con id: " + id));
    }

    public LibroResponse crearLibro(LibroRequest request) {
        log.info("Creando nuevo libro: {}", request.getTitulo());
        Libro libro = mapper.toEntity(request);

        AutorResponse autor = autorClient.buscarPorId(libro.getIdAutor());
        if (autor == null) {
            log.warn("Autor con id: {} no encontrado", libro.getIdAutor());
            throw new NoSuchElementException("Autor con id " + libro.getIdAutor() + " no encontrado");
        }

        GeneroResponse genero = generoClient.buscarPorId(libro.getIdGenero());
        if (genero == null) {
            log.warn("Genero con id: {} no encontrado", libro.getIdGenero());
            throw new NoSuchElementException("Genero con id " + libro.getIdGenero() + " no encontrado");
        }

        return mapper.toResponse(repository.save(libro), genero, autor);
    }

    public LibroResponse actualizarLibro(Long id, LibroRequest request) {
        log.info("Actualizando libro con id: {}", id);

        Libro libroExistente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró libro con id " + id));

        AutorResponse autor = autorClient.buscarPorId(request.getIdAutor());
        if (autor == null) {
            log.warn("Autor con id: {} no encontrado", request.getIdAutor());
            throw new NoSuchElementException("Autor con id " + request.getIdAutor() + " no encontrado");
        }

        GeneroResponse genero = generoClient.buscarPorId(request.getIdGenero());
        if (genero == null) {
            log.warn("Genero con id: {} no encontrado", request.getIdGenero());
            throw new NoSuchElementException("Genero con id " + request.getIdGenero() + " no encontrado");
        }

        libroExistente.setTitulo(request.getTitulo());
        libroExistente.setIsbn(request.getIsbn());
        libroExistente.setIdGenero(genero.getId());
        libroExistente.setIdAutor(autor.getId());
        libroExistente.setEstado(request.getEstado());

        Libro libroActualizado = repository.save(libroExistente);
        return mapper.toResponse(libroActualizado, genero, autor);
    }

    public void eliminarLibro(Long id) {
        log.info("Eliminando libro con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró libro con id: " + id);
        }
        repository.deleteById(id);
    }

    public List<LibroResponse> buscarLibroPorAutor(Long idAutor) {
        log.info("Buscando libros del autor con id: {}", idAutor);
        return repository.findByIdAutor(idAutor).stream()
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarPorId(libro.getIdAutor());
                    GeneroResponse genero = generoClient.buscarPorId(libro.getIdGenero());
                    return mapper.toResponse(libro, genero, autor);
                })
                .toList();
    }

    public List<LibroResponse> buscarLibroPorGenero(Long idGenero) {
        log.info("Buscando libros del genero con id: {}", idGenero);
        return repository.findByIdGenero(idGenero).stream()
                .map(libro -> {
                    AutorResponse autor = autorClient.buscarPorId(libro.getIdAutor());
                    GeneroResponse genero = generoClient.buscarPorId(libro.getIdGenero());
                    return mapper.toResponse(libro, genero, autor);
                })
                .toList();
    }

}
