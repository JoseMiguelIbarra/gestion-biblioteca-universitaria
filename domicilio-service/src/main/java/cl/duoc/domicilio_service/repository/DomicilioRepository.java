package cl.duoc.domicilio_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.domicilio_service.model.Domicilio;

@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, Long>{

}
