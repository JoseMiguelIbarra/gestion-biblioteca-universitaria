package cl.duoc.genero_service.controller;

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

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.service.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("/generos")
@Tag(name = "Generos", description = "Operaciones para la gestion de generos literarios")
public class GeneroController {

    @Autowired
    private GeneroService service;

    @Operation(summary = "Listar generos",
               description = "Devuelve la lista completa de generos registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de generos obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<GeneroResponse>> getGeneros() {
        log.info("GET/generos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar genero por ID",
               description = "Devuelve un genero segun su identificador unico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Genero encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un genero con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GeneroResponse> getGenerosById(
            @Parameter(description = "ID del genero a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/generos/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear genero",
               description = "Registra un nuevo genero. Devuelve el recurso creado y la cabecera Location con su URL.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Genero creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)")
    })
    @PostMapping
    public ResponseEntity<GeneroResponse> postGenero(@Valid @RequestBody GeneroRequest genero) {
        log.info("POST/generos");
        GeneroResponse generoCreado = service.crearGenero(genero);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(generoCreado.getId())
                .toUri();
        return ResponseEntity.created(location).body(generoCreado);
    }

    @Operation(summary = "Eliminar genero",
               description = "Elimina un genero del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Genero eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un genero con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del genero a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/generos/{}", id);
        service.eliminarGenero(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar genero",
               description = "Modifica los datos de un genero existente segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Genero actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "No existe un genero con el ID indicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GeneroResponse> actualizar(
            @Parameter(description = "ID del genero a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody GeneroRequest request) {
        log.info("PUT/generos/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

}