package cl.duoc.genero_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.genero_service.dto.GeneroRequest;
import cl.duoc.genero_service.dto.GeneroResponse;
import cl.duoc.genero_service.model.Genero;

@Component
public class GeneroMapper {

    // de JSON a Entidad MySQL
    public Genero toEntity (GeneroRequest request){
        return Genero.builder()
            .nombre(request.getNombre())
            .build();
    }

    // de Entidad MySQL a JSON
    public GeneroResponse toResponse (Genero genero){
        return GeneroResponse.builder()
            .id(genero.getId())
            .nombre(genero.getNombre())
            .build();
    }

}
