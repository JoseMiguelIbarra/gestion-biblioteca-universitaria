package cl.duoc.bodega_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.bodega_service.model.Bodega;

@Repository
public interface BodegaRepository extends JpaRepository <Bodega, Long>{

}
