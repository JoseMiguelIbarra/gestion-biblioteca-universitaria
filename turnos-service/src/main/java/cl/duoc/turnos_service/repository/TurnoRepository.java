package cl.duoc.turnos_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.turnos_service.model.Turno;
import cl.duoc.turnos_service.model.TipoTurno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByIdBibliotecario(Long idBibliotecario);

    List<Turno> findByFechaTurno(LocalDate fechaTurno);

    List<Turno> findByTipoTurno(TipoTurno tipoTurno);

}
