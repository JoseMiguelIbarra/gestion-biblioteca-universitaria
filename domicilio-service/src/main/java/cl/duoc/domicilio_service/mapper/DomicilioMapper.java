package cl.duoc.domicilio_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.domicilio_service.dto.DomicilioRequest;
import cl.duoc.domicilio_service.dto.DomicilioResponse;
import cl.duoc.domicilio_service.model.Domicilio;


@Component
public class DomicilioMapper {

    public Domicilio toEntity (DomicilioRequest request){
        return Domicilio.builder()
        .direccion(request.getDireccion())
        .comuna(request.getComuna())
        .pais(request.getPais())
        .build();
    }

    public DomicilioResponse toResponse(Domicilio domicilio){
        return DomicilioResponse.builder()
        .id(domicilio.getId())
        .direccion(domicilio.getDireccion())
        .comuna(domicilio.getComuna())
        .pais(domicilio.getPais())
        .build();
    }

}
