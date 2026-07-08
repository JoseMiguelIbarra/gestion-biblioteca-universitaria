package cl.duoc.turnos_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import cl.duoc.turnos_service.model.TipoTurno;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TurnoRequest {

    @NotNull(message = "El id del bibliotecario es obligatorio")
    private Long idBibliotecario;

    @NotNull(message = "La fecha del turno es obligatoria")
    @FutureOrPresent(message = "La fecha del turno debe ser una fecha futura o actual")
    private LocalDate fechaTurno;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    @NotNull(message = "El tipo de turno es obligatorio")
    private TipoTurno tipoTurno;

}
