package cl.duoc.bodega_service.controller;

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

import cl.duoc.bodega_service.dto.BodegaRequest;
import cl.duoc.bodega_service.dto.BodegaResponse;
import cl.duoc.bodega_service.service.BodegaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/bodegas")
@Tag(name = "Bodegas", description = "Operaciones para la gestion de bodegas de la biblioteca")
public class BodegaController {

    @Autowired
    private BodegaService service;

    @Operation(summary = "Listar bodegas",
               description = "Devuelve la lista completa de bodegas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de bodegas obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<BodegaResponse>> getBodegas() {
        log.info("GET/bodegas");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @Operation(summary = "Buscar bodega por ID",
               description = "Devuelve una bodega segun su identificador unico.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bodega encontrada"),
        @ApiResponse(responseCode = "404", description = "No existe una bodega con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BodegaResponse> getBodegaPorId(
            @Parameter(description = "ID de la bodega a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/bodegas/{}", id);
        return ResponseEntity.ok(service.buscarBodegaPorId(id));
    }

    @Operation(summary = "Crear bodega",
               description = "Registra una nueva bodega. Devuelve el recurso creado y la cabecera Location con su URL.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Bodega creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)")
    })
    @PostMapping
    public ResponseEntity<BodegaResponse> postBodega(@Valid @RequestBody BodegaRequest request) {
        log.info("POST/bodegas");
        BodegaResponse bodegaCreada = service.crear(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bodegaCreada.getId())
                .toUri();

        return ResponseEntity.created(location).body(bodegaCreada);
    }

    @Operation(summary = "Actualizar bodega",
               description = "Modifica los datos de una bodega existente segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bodega actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "No existe una bodega con el ID indicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BodegaResponse> putBodega(
            @Parameter(description = "ID de la bodega a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody BodegaRequest request) {
        log.info("PUT/bodegas/{}", id);
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar bodega",
               description = "Elimina una bodega del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Bodega eliminada correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe una bodega con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBodega(
            @Parameter(description = "ID de la bodega a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/bodegas/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}