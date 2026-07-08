package cl.duoc.estante_service.controller;

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

import cl.duoc.estante_service.dto.EstanteRequest;
import cl.duoc.estante_service.dto.EstanteResponse;
import cl.duoc.estante_service.service.EstanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/estantes")
@Tag(name = "Estantes", description = "Operaciones para la gestion de estantes; integra datos de la bodega asociada")
public class EstanteController {

    @Autowired
    private EstanteService service;

    @Operation(summary = "Listar estantes",
               description = "Devuelve todos los estantes, enriquecidos con los datos de su bodega "
                       + "obtenidos desde bodega-service.")
    @ApiResponse(responseCode = "200", description = "Lista de estantes obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<EstanteResponse>> obtenerTodos() {
        log.info("GET/estantes");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar estante por ID",
               description = "Devuelve un estante segun su identificador, incluyendo su bodega.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estante encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un estante con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstanteResponse> obtenerEstantePorId(
            @Parameter(description = "ID del estante a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET/estantes/{}", id);
        return ResponseEntity.ok(service.buscarEstantePorId(id));
    }

    @Operation(summary = "Listar estantes por bodega",
               description = "Devuelve todos los estantes asociados a una bodega segun su ID.")
    @ApiResponse(responseCode = "200", description = "Lista de estantes de la bodega obtenida correctamente")
    @GetMapping("/bodega/{idBodega}")
    public ResponseEntity<List<EstanteResponse>> obtenerEstantesPorIdBodega(
            @Parameter(description = "ID de la bodega", example = "2")
            @PathVariable Long idBodega) {
        log.info("GET/estantes/bodega/{}", idBodega);
        return ResponseEntity.ok(service.buscarEstantePorIdBodega(idBodega));
    }

    @Operation(summary = "Crear estante",
               description = "Registra un nuevo estante. Valida que la bodega indicada exista en bodega-service antes de guardar.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Estante creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "La bodega indicada no existe")
    })
    @PostMapping
    public ResponseEntity<EstanteResponse> crearEstante(@Valid @RequestBody EstanteRequest request) {
        log.info("POST/estantes");
        EstanteResponse estanteCreado = service.crearEstante(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(estanteCreado.getId())
                .toUri();

        return ResponseEntity.created(location).body(estanteCreado);
    }

    @Operation(summary = "Actualizar estante",
               description = "Modifica un estante existente. Valida que el estante y la bodega existan.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estante actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos (validacion fallida)"),
        @ApiResponse(responseCode = "404", description = "El estante o la bodega indicada no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstanteResponse> actualizarEstante(
            @Parameter(description = "ID del estante a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EstanteRequest request) {
        log.info("PUT/estantes/{}", id);
        return ResponseEntity.ok(service.actualizarEstante(id, request));
    }

    @Operation(summary = "Eliminar estante",
               description = "Elimina un estante del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estante eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un estante con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstante(
            @Parameter(description = "ID del estante a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE/estantes/{}", id);
        service.eliminarEstante(id);
        return ResponseEntity.noContent().build();
    }

}