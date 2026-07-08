package cl.duoc.genero_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.genero_service.model.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long>{
}
