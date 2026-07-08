package cl.duoc.libro_service.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.libro_service.dto.AutorResponse;
import cl.duoc.libro_service.dto.GeneroResponse;
import cl.duoc.libro_service.dto.LibroRequest;
import cl.duoc.libro_service.dto.LibroResponse;
import cl.duoc.libro_service.model.Libro;

@Component
public class LibroMapper {

    public Libro toEntity(LibroRequest request) {
        return Libro.builder()
                .titulo(request.getTitulo())
                .isbn(request.getIsbn())
                .idGenero(request.getIdGenero())
                .idAutor(request.getIdAutor())
                .estado(request.getEstado())
                .build();
    }

    public LibroResponse toResponse (Libro libro, GeneroResponse genResponse, AutorResponse autResponse){
        return LibroResponse.builder()
            .id(libro.getId())
            .titulo(libro.getTitulo())
            .isbn(libro.getIsbn())
            .genero(genResponse)
            .autor(autResponse)
            .estado(libro.getEstado())
            .build();
    }
}
