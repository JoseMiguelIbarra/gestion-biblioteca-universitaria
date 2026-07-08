package cl.duoc.nombre_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.nombre_service.dto.NombreRequest;
import cl.duoc.nombre_service.dto.NombreResponse;
import cl.duoc.nombre_service.model.Nombre;

@Component
public class NombreMapper {

    public Nombre toEntity (NombreRequest request){
        return Nombre.builder()
                .nombre(request.getNombre())
                .segundoNombre(request.getSegundoNombre())
                .apPaterno(request.getApPaterno())
                .apMaterno(request.getApMaterno())
                .build();
    }

    public NombreResponse toResponse (Nombre nombre){
        return NombreResponse.builder()
                .id(nombre.getId())
                .nombre(nombre.getNombre())
                .segundoNombre(nombre.getSegundoNombre())
                .apPaterno(nombre.getApPaterno())
                .apMaterno(nombre.getApMaterno())
                .build();
    }

}
