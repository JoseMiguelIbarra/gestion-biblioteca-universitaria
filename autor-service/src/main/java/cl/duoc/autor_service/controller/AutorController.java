package cl.duoc.autor_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.service.AutorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Slf4j
@RequestMapping("/autores")
@Tag(name = "Autores", description = "Operaciones para la gestión de autores de la biblioteca")
public class AutorController {

    @Autowired
    private AutorService service;

    @Operation(summary = "Listar autores",
               description = "Devuelve la lista completa de autores registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de autores obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<AutorResponse>> getAutores() {
        log.info("GET/autores");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar autor por ID",
               description = "Devuelve un autor segun su identificador unico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autor encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un autor con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> getAutorPorId(
            @Parameter(description = "ID del autor a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/autores/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear autor",
               description = "Registra un nuevo autor. Devuelve el autor creado y la cabecera Location con su URL.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Autor creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)")
    })
    @PostMapping
    public ResponseEntity<AutorResponse> postAutor(@Valid @RequestBody AutorRequest request) {
        log.info("POST/autores");
        AutorResponse autorCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(autorCreado);
    }

    @Operation(summary = "Actualizar autor",
               description = "Modifica los datos de un autor existente segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autor actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "No existe un autor con el ID indicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponse> putAutor(
            @Parameter(description = "ID del autor a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AutorRequest request) {
        log.info("PUT/autores/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar autor",
               description = "Elimina un autor del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Autor eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un autor con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(
            @Parameter(description = "ID del autor a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/autores/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}