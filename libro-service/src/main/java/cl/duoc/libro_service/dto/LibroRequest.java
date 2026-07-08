package cl.duoc.libro_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibroRequest {

    @NotEmpty(message = "El título no puede estar vacío..")
    @Size(max = 100, message = "El máximo es de 100 caracteres.")
    private String titulo;

    @NotEmpty(message = "El ISBN no puede estar vacío.")
    @Size(max = 100, message = "El máximo es de 100 caracteres.")
    private String isbn;

    @NotNull(message = "El ID no puede ser nulo.")
    private Long idGenero;

    @NotNull(message = "El ID no puede ser nulo.")
    private Long idAutor;

    @NotNull(message = "El estado no puede ser nulo")
    private Boolean estado;

}
