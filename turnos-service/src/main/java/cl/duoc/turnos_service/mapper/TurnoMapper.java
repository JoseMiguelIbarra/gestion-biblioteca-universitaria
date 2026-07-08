package cl.duoc.turnos_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.turnos_service.dto.BibliotecarioResponse;
import cl.duoc.turnos_service.dto.TurnoRequest;
import cl.duoc.turnos_service.dto.TurnoResponse;
import cl.duoc.turnos_service.model.Turno;

@Component
public class TurnoMapper {

    // Transforma un request a entidad.
    public Turno toEntity(TurnoRequest request) {
        return Turno.builder()
                .idBibliotecario(request.getIdBibliotecario())
                .fechaTurno(request.getFechaTurno())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .tipoTurno(request.getTipoTurno())
                .build();
    }

    // Transforma una entidad a response enriquecida con datos del bibliotecario.
    public TurnoResponse toResponse(Turno turno, BibliotecarioResponse bibliotecario) {
        return TurnoResponse.builder()
                .id(turno.getId())
                .bibliotecario(bibliotecario)
                .fechaTurno(turno.getFechaTurno())
                .horaInicio(turno.getHoraInicio())
                .horaFin(turno.getHoraFin())
                .tipoTurno(turno.getTipoTurno())
                .build();
    }

}
