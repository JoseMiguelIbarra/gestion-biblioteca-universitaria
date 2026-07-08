package cl.duoc.libro_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/libros")
@Tag(name = "Libros", description = "Operaciones para la gestion de libros; integra datos de autor y genero")
public class LibroController {

    @Autowired
    private LibroService service;

    @Operation(summary = "Listar libros",
               description = "Devuelve todos los libros, enriquecidos con los datos de su autor y genero "
                       + "obtenidos desde los microservicios correspondientes.")
    @ApiResponse(responseCode = "200", description = "Lista de libros obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<LibroResponse>> obtenerTodos() {
        log.info("GET/libros");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar libro por ID",
               description = "Devuelve un libro segun su identificador, incluyendo su autor y genero.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un libro con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtenerLibroPorId(
            @Parameter(description = "ID del libro a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/libros/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Listar libros por autor",
               description = "Devuelve todos los libros asociados a un autor segun su ID.")
    @ApiResponse(responseCode = "200", description = "Lista de libros del autor obtenida correctamente")
    @GetMapping("/autor/{idAutor}")
    public ResponseEntity<List<LibroResponse>> obtenerLibrosPorIdAutor(
            @Parameter(description = "ID del autor", example = "3")
            @PathVariable Long idAutor) {
        log.info("GET/libros/autor/{}", idAutor);
        return ResponseEntity.ok(service.buscarLibroPorAutor(idAutor));
    }

    @Operation(summary = "Listar libros por genero",
               description = "Devuelve todos los libros asociados a un genero segun su ID.")
    @ApiResponse(responseCode = "200", description = "Lista de libros del genero obtenida correctamente")
    @GetMapping("/genero/{idGenero}")
    public ResponseEntity<List<LibroResponse>> obtenerLibrosPorIdGenero(
            @Parameter(description = "ID del genero", example = "2")
            @PathVariable Long idGenero) {
        log.info("GET/libros/genero/{}", idGenero);
        return ResponseEntity.ok(service.buscarLibroPorGenero(idGenero));
    }

    @Operation(summary = "Crear libro",
               description = "Registra un nuevo libro. Valida que el autor y el genero indicados existan "
                       + "en sus respectivos microservicios antes de guardar.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Libro creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "El autor o el genero indicado no existe")
    })
    @PostMapping
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody LibroRequest request) {
        log.info("POST/libros");
        LibroResponse libroCreado = service.crearLibro(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(libroCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(libroCreado);
    }

    @Operation(summary = "Actualizar libro",
               description = "Modifica un libro existente. Valida que el libro, el autor y el genero existan.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "El libro, el autor o el genero indicado no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizarLibro(
            @Parameter(description = "ID del libro a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody LibroRequest request) {
        log.info("PUT/libros/{}", id);
        return ResponseEntity.ok(service.actualizarLibro(id, request));
    }

    @Operation(summary = "Eliminar libro",
               description = "Elimina un libro del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Libro eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un libro con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(
            @Parameter(description = "ID del libro a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/libros/{}", id);
        service.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

}