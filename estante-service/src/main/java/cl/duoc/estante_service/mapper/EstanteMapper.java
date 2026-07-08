package cl.duoc.estante_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.estante_service.dto.BodegaResponse;
import cl.duoc.estante_service.dto.EstanteRequest;
import cl.duoc.estante_service.dto.EstanteResponse;
import cl.duoc.estante_service.model.Estante;

@Component
public class EstanteMapper {

    public Estante toEntity(EstanteRequest request){
        return Estante.builder()
                .numero(request.getNumero())
                .idBodega(request.getIdBodega())
                .capacidad(request.getCapacidad())
                .build();
    }

    public EstanteResponse toResponse (Estante estante, BodegaResponse bodResponse){
        return EstanteResponse.builder()
                .id(estante.getId())
                .numero(estante.getNumero())
                .bodega(bodResponse)
                .capacidad(estante.getCapacidad())
                .build();
    }

}
