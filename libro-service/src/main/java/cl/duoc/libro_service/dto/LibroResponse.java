package cl.duoc.libro_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponse {
    private Long id;
    private String titulo;
    private String isbn;
    private GeneroResponse genero;
    private AutorResponse autor;
    private Boolean estado; 
}
