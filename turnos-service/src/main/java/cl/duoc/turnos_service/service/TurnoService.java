package cl.duoc.turnos_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.turnos_service.client.BibliotecarioClient;
import cl.duoc.turnos_service.dto.BibliotecarioResponse;
import cl.duoc.turnos_service.dto.TurnoRequest;
import cl.duoc.turnos_service.dto.TurnoResponse;
import cl.duoc.turnos_service.mapper.TurnoMapper;
import cl.duoc.turnos_service.model.Turno;
import cl.duoc.turnos_service.model.TipoTurno;
import cl.duoc.turnos_service.repository.TurnoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TurnoService {

    @Autowired
    private TurnoRepository repository;

    @Autowired
    private TurnoMapper mapper;

    @Autowired
    private BibliotecarioClient bibliotecarioClient;

    public List<TurnoResponse> obtenerTodos() {
        log.info("Consultando turnos...");
        return repository.findAll().stream().map(turno -> {
            BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(turno.getIdBibliotecario());
            return mapper.toResponse(turno, bibliotecario);
        }).toList();
    }

    public TurnoResponse buscarPorId(Long id) {
        log.info("Buscando turno con id: {}", id);
        return repository.findById(id)
                .map(turno -> {
                    BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(turno.getIdBibliotecario());
                    return mapper.toResponse(turno, bibliotecario);
                })
                .orElseThrow(() -> new NoSuchElementException("No se encontró turno con id: " + id));
    }

    public List<TurnoResponse> buscarPorBibliotecario(Long idBibliotecario) {
        log.info("Buscando turnos del bibliotecario con id: {}", idBibliotecario);
        // Valida existencia y reutiliza el resultado para mapear todos los turnos
        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(idBibliotecario);
        return repository.findByIdBibliotecario(idBibliotecario).stream()
                .map(turno -> mapper.toResponse(turno, bibliotecario))
                .toList();
    }

    public List<TurnoResponse> buscarPorFecha(LocalDate fechaTurno) {
        log.info("Buscando turnos de la fecha: {}", fechaTurno);
        return repository.findByFechaTurno(fechaTurno).stream()
                .map(turno -> {
                    BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(turno.getIdBibliotecario());
                    return mapper.toResponse(turno, bibliotecario);
                })
                .toList();
    }

    public List<TurnoResponse> buscarPorTipo(TipoTurno tipoTurno) {
        log.info("Buscando turnos de tipo: {}", tipoTurno);
        return repository.findByTipoTurno(tipoTurno).stream()
                .map(turno -> {
                    BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(turno.getIdBibliotecario());
                    return mapper.toResponse(turno, bibliotecario);
                })
                .toList();
    }

    public TurnoResponse crearTurno(TurnoRequest request) {
        log.info("Creando nuevo turno para bibliotecario con id: {}", request.getIdBibliotecario());

        if (request.getHoraFin().isBefore(request.getHoraInicio()) ||
                request.getHoraFin().equals(request.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }

        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(request.getIdBibliotecario());

        Turno turno = mapper.toEntity(request);
        return mapper.toResponse(repository.save(turno), bibliotecario);
    }

    public TurnoResponse actualizarTurno(Long id, TurnoRequest request) {
        log.info("Actualizando turno con id: {}", id);

        Turno turnoExistente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró turno con id: " + id));

        if (request.getHoraFin().isBefore(request.getHoraInicio()) ||
                request.getHoraFin().equals(request.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }

        BibliotecarioResponse bibliotecario = bibliotecarioClient.buscarPorId(request.getIdBibliotecario());

        turnoExistente.setIdBibliotecario(request.getIdBibliotecario());
        turnoExistente.setFechaTurno(request.getFechaTurno());
        turnoExistente.setHoraInicio(request.getHoraInicio());
        turnoExistente.setHoraFin(request.getHoraFin());
        turnoExistente.setTipoTurno(request.getTipoTurno());

        return mapper.toResponse(repository.save(turnoExistente), bibliotecario);
    }

    public void eliminarTurno(Long id) {
        log.info("Eliminando turno con id: {}", id);
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("No se encontró turno con id: " + id);
        }
        repository.deleteById(id);
    }

}
