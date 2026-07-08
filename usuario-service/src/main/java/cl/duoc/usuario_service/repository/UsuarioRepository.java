package cl.duoc.usuario_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.usuario_service.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    List<Usuario> findByIdNombre(Long idNombre);
    List<Usuario> findByIdDomicilio(Long idDomicilio);
}
