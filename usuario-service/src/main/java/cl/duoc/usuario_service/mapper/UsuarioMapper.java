package cl.duoc.usuario_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.usuario_service.dto.DomicilioResponse;
import cl.duoc.usuario_service.dto.NombreResponse;
import cl.duoc.usuario_service.dto.UsuarioRequest;
import cl.duoc.usuario_service.dto.UsuarioResponse;
import cl.duoc.usuario_service.model.Usuario;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        return Usuario.builder()
                .idNombre(request.getIdNombre())
                .rut(request.getRut())
                .edad(request.getEdad())
                .idDomicilio(request.getIdDomicilio())
                .fechaNacimiento(request.getFechaNacimiento())
                .build();
    }

    public UsuarioResponse toResponse (Usuario usuario, DomicilioResponse domResponse, NombreResponse nomResponse){
        return UsuarioResponse.builder()
            .id(usuario.getId())
            .nombre(nomResponse)
            .rut(usuario.getRut())
            .edad(usuario.getEdad())
            .domicilio(domResponse)
            .fechaNacimiento(usuario.getFechaNacimiento())
            .build();
    }

}
