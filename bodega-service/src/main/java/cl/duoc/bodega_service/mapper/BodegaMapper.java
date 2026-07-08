package cl.duoc.bodega_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.bodega_service.dto.BodegaRequest;
import cl.duoc.bodega_service.dto.BodegaResponse;
import cl.duoc.bodega_service.model.Bodega;

@Component
public class BodegaMapper {

    // Transformar de JSON a Entidad MySQL
    public Bodega toEntity (BodegaRequest request){
        return Bodega.builder()
            .nombre(request.getNombre())
            .ubicacion(request.getUbicacion())
            .capacidad(request.getCapacidad())
            .build();
    }

    // Transformar de Entidad MySQL a JSON
    public BodegaResponse toResponse (Bodega bodega){
        return BodegaResponse.builder()
            .id(bodega.getId())
            .nombre(bodega.getNombre())
            .ubicacion(bodega.getUbicacion())
            .capacidad(bodega.getCapacidad())
            .build();
    }

}
