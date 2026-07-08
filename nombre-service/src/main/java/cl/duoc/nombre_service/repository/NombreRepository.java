package cl.duoc.nombre_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.nombre_service.model.Nombre;

@Repository
public interface NombreRepository extends JpaRepository <Nombre, Long>{

}
