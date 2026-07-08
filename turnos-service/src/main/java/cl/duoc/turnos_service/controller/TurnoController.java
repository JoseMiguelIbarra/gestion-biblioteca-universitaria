package cl.duoc.turnos_service.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import cl.duoc.turnos_service.dto.TurnoRequest;
import cl.duoc.turnos_service.dto.TurnoResponse;
import cl.duoc.turnos_service.model.TipoTurno;
import cl.duoc.turnos_service.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/turnos")
@Tag(name = "Turnos", description = "Operaciones para la gestion de turnos de bibliotecarios; integra datos del bibliotecario asociado")
public class TurnoController {

    @Autowired
    private TurnoService service;

    @Operation(summary = "Listar turnos",
               description = "Devuelve todos los turnos, enriquecidos con los datos del bibliotecario "
                       + "obtenidos desde bibliotecario-service.")
    @ApiResponse(responseCode = "200", description = "Lista de turnos obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<TurnoResponse>> obtenerTodos() {
        log.info("GET /turnos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @Operation(summary = "Buscar turno por ID",
               description = "Devuelve un turno segun su identificador, incluyendo su bibliotecario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Turno encontrado"),
        @ApiResponse(responseCode = "404", description = "No existe un turno con el ID indicado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TurnoResponse> obtenerPorId(
            @Parameter(description = "ID del turno a buscar", example = "1")
            @PathVariable Long id) {
        log.info("GET /turnos/{}", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Listar turnos por bibliotecario",
               description = "Devuelve todos los turnos asociados a un bibliotecario segun su ID. "
                       + "Valida que el bibliotecario exista en bibliotecario-service.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de turnos del bibliotecario obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "El bibliotecario indicado no existe")
    })
    @GetMapping("/bibliotecario/{idBibliotecario}")
    public ResponseEntity<List<TurnoResponse>> obtenerPorBibliotecario(
            @Parameter(description = "ID del bibliotecario", example = "1")
            @PathVariable Long idBibliotecario) {
        log.info("GET /turnos/bibliotecario/{}", idBibliotecario);
        return ResponseEntity.ok(service.buscarPorBibliotecario(idBibliotecario));
    }

    @Operation(summary = "Listar turnos por fecha",
               description = "Devuelve todos los turnos de una fecha especifica (formato ISO: yyyy-MM-dd).")
    @ApiResponse(responseCode = "200", description = "Lista de turnos de la fecha obtenida correctamente")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<TurnoResponse>> obtenerPorFecha(
            @Parameter(description = "Fecha en formato ISO (yyyy-MM-dd)", example = "2026-07-01")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        log.info("GET /turnos/fecha/{}", fecha);
        return ResponseEntity.ok(service.buscarPorFecha(fecha));
    }

    @Operation(summary = "Listar turnos por tipo",
               description = "Devuelve todos los turnos de un tipo determinado (por ejemplo, MANANA o TARDE).")
    @ApiResponse(responseCode = "200", description = "Lista de turnos del tipo obtenida correctamente")
    @GetMapping("/tipo/{tipoTurno}")
    public ResponseEntity<List<TurnoResponse>> obtenerPorTipo(
            @Parameter(description = "Tipo de turno", example = "MANANA")
            @PathVariable TipoTurno tipoTurno) {
        log.info("GET /turnos/tipo/{}", tipoTurno);
        return ResponseEntity.ok(service.buscarPorTipo(tipoTurno));
    }

    @Operation(summary = "Crear turno",
               description = "Registra un nuevo turno. Valida que el bibliotecario exista y que la hora de "
                       + "fin sea posterior a la hora de inicio.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Turno creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos: validacion fallida u horario incorrecto (hora de fin no posterior a la de inicio)"),
        @ApiResponse(responseCode = "404", description = "El bibliotecario indicado no existe")
    })
    @PostMapping
    public ResponseEntity<TurnoResponse> crearTurno(@Valid @RequestBody TurnoRequest request) {
        log.info("POST /turnos");
        TurnoResponse creado = service.crearTurno(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Actualizar turno",
               description = "Modifica un turno existente. Valida que el turno y el bibliotecario existan y "
                       + "que la hora de fin sea posterior a la hora de inicio.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Turno actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos: validacion fallida u horario incorrecto"),
        @ApiResponse(responseCode = "404", description = "El turno o el bibliotecario indicado no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TurnoResponse> actualizarTurno(
            @Parameter(description = "ID del turno a actualizar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TurnoRequest request) {
        log.info("PUT /turnos/{}", id);
        return ResponseEntity.ok(service.actualizarTurno(id, request));
    }

    @Operation(summary = "Eliminar turno",
               description = "Elimina un turno del sistema segun su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Turno eliminado correctamente (sin contenido)"),
        @ApiResponse(responseCode = "404", description = "No existe un turno con el ID indicado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTurno(
            @Parameter(description = "ID del turno a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /turnos/{}", id);
        service.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }

}