package cl.duoc.nombre_service.controller;

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

import cl.duoc.nombre_service.dto.NombreRequest;
import cl.duoc.nombre_service.dto.NombreResponse;
import cl.duoc.nombre_service.service.NombreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/nombres")
@Tag(name = "Nombres", description = "Operaciones para la gestion de nombres de los usuarios")
public class NombreController {

    @Autowired
    private NombreService service;

    @Operation(summary = "Listar nombres",
               description = "Devuelve la lista completa de nombres registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de nombres obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<NombreResponse>> getNombres() {
        log.info("GET/nombres");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar nombre por ID",
               description = "Devuelve un nombre segun su identificador unico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un nombre con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NombreResponse> getNombrePorId(
            @Parameter(description = "ID del nombre a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/nombres/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear nombre",
               description = "Registra un nuevo nombre. Devuelve el recurso creado y la cabecera Location con su URL.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Nombre creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)")
    })
    @PostMapping
    public ResponseEntity<NombreResponse> postNombre(@Valid @RequestBody NombreRequest request) {
        log.info("POST/nombres");
        NombreResponse nombreCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nombreCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(nombreCreado);
    }

    @Operation(summary = "Actualizar nombre",
               description = "Modifica los datos de un nombre existente segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "No existe un nombre con el ID indicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NombreResponse> putNombre(
            @Parameter(description = "ID del nombre a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody NombreRequest request) {
        log.info("PUT/nombres/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar nombre",
               description = "Elimina un nombre del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Nombre eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un nombre con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNombre(
            @Parameter(description = "ID del nombre a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/nombres/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}