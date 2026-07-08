package cl.duoc.libro_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.libro_service.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository <Libro, Long>{
    List<Libro> findByIdAutor(Long idAutor);
    List<Libro> findByIdGenero(Long idGenero);
}
