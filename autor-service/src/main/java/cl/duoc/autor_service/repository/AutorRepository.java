package cl.duoc.autor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.autor_service.model.Autor;

@Repository
public interface AutorRepository extends JpaRepository <Autor, Long>{

}
