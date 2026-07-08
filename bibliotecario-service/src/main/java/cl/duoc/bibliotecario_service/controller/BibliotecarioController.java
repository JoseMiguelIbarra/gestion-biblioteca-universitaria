package cl.duoc.bibliotecario_service.controller;

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

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.service.BibliotecarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/bibliotecarios")
@Tag(name = "Bibliotecarios", description = "Operaciones para la gestion de bibliotecarios de la biblioteca")
public class BibliotecarioController {

    @Autowired
    private BibliotecarioService service;

    @Operation(summary = "Listar bibliotecarios",
               description = "Devuelve la lista completa de bibliotecarios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de bibliotecarios obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<BibliotecarioResponse>> obtenerTodos() {
        log.info("GET/bibliotecarios");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar bibliotecario por ID",
               description = "Devuelve un bibliotecario segun su identificador unico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bibliotecario encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un bibliotecario con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BibliotecarioResponse> obtenerPorId(
            @Parameter(description = "ID del bibliotecario a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/bibliotecarios/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear bibliotecario",
               description = "Registra un nuevo bibliotecario. Devuelve el recurso creado y la cabecera Location con su URL.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Bibliotecario creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)")
    })
    @PostMapping
    public ResponseEntity<BibliotecarioResponse> crear(@Valid @RequestBody BibliotecarioRequest request) {
        log.info("POST/bibliotecarios");
        BibliotecarioResponse creado = service.crear(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Actualizar bibliotecario",
               description = "Modifica los datos de un bibliotecario existente segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bibliotecario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "No existe un bibliotecario con el ID indicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BibliotecarioResponse> actualizar(
            @Parameter(description = "ID del bibliotecario a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BibliotecarioRequest request) {
        log.info("PUT/bibliotecarios/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar bibliotecario",
               description = "Elimina un bibliotecario del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Bibliotecario eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un bibliotecario con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del bibliotecario a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/bibliotecarios/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}