package cl.duoc.domicilio_service.controller;

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

import cl.duoc.domicilio_service.dto.DomicilioRequest;
import cl.duoc.domicilio_service.dto.DomicilioResponse;
import cl.duoc.domicilio_service.service.DomicilioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/domicilios")
@Tag(name = "Domicilios", description = "Operaciones para la gestion de domicilios de los usuarios")
public class DomicilioController {

    @Autowired
    private DomicilioService service;

    @Operation(summary = "Listar domicilios",
               description = "Devuelve la lista completa de domicilios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de domicilios obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<DomicilioResponse>> getDomicilio() {
        log.info("GET/domicilios");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar domicilio por ID",
               description = "Devuelve un domicilio segun su identificador unico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Domicilio encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un domicilio con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DomicilioResponse> getDomicilioPorId(
            @Parameter(description = "ID del domicilio a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/domicilios/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Crear domicilio",
               description = "Registra un nuevo domicilio. Devuelve el recurso creado y la cabecera Location con su URL.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Domicilio creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)")
    })
    @PostMapping
    public ResponseEntity<DomicilioResponse> postDomicilio(@Valid @RequestBody DomicilioRequest request) {
        log.info("POST/domicilios");
        DomicilioResponse domicilioCreado = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(domicilioCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(domicilioCreado);
    }

    @Operation(summary = "Actualizar domicilio",
               description = "Modifica los datos de un domicilio existente segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Domicilio actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "No existe un domicilio con el ID indicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DomicilioResponse> putDomicilio(
            @Parameter(description = "ID del domicilio a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody DomicilioRequest request) {
        log.info("PUT/domicilios/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar domicilio",
               description = "Elimina un domicilio del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Domicilio eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un domicilio con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomicilio(
            @Parameter(description = "ID del domicilio a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/domicilios/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}