package cl.duoc.estante_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.estante_service.model.Estante;

public interface EstanteRepository extends JpaRepository <Estante, Long>{
    List<Estante> findByIdBodega(Long idBodega);

}
