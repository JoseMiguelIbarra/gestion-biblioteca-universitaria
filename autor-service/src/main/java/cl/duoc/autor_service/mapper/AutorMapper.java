package cl.duoc.autor_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.autor_service.dto.AutorRequest;
import cl.duoc.autor_service.dto.AutorResponse;
import cl.duoc.autor_service.model.Autor;

@Component
public class AutorMapper {
    
    // Transformar de JSON a Entidad MySQL
    public Autor toEntity (AutorRequest request){
        return Autor.builder()
            .nombre(request.getNombre())
            .fecha_nac(request.getFecha_nac())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public AutorResponse toResponse (Autor autor){
        return AutorResponse.builder()
            .id(autor.getId())
            .nombre(autor.getNombre())
            .fecha_nac(autor.getFecha_nac())
            .build();
    }

}
