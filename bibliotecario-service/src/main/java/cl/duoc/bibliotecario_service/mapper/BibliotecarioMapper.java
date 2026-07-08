package cl.duoc.bibliotecario_service.mapper;
// mapper/BibliotecarioMapper.java

import org.springframework.stereotype.Component;

import cl.duoc.bibliotecario_service.dto.BibliotecarioRequest;
import cl.duoc.bibliotecario_service.dto.BibliotecarioResponse;
import cl.duoc.bibliotecario_service.model.Bibliotecario;


@Component
public class BibliotecarioMapper {

    // Transformar una request a entidad.
    public Bibliotecario toEntity(BibliotecarioRequest request) {
        return Bibliotecario.builder()
                .nombre(request.getNombre())
                .edad(request.getEdad())
                .fechaIngreso(request.getFechaIngreso())
                .build();
    }

    // Transformar una entidad a una response.
    public BibliotecarioResponse toResponse(Bibliotecario bibliotecario) {
        return BibliotecarioResponse.builder()
                .id(bibliotecario.getId())
                .nombre(bibliotecario.getNombre())
                .edad(bibliotecario.getEdad())
                .fechaIngreso(bibliotecario.getFechaIngreso())
                .build();
    }
}