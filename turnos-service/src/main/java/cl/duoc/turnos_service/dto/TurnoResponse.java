package cl.duoc.turnos_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import cl.duoc.turnos_service.model.TipoTurno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TurnoResponse {

    private Long id;
    private BibliotecarioResponse bibliotecario;
    private LocalDate fechaTurno;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private TipoTurno tipoTurno;

}
