package cl.duoc.genero_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneroRequest {

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El máximo es de 100 caracteres.")
    private String nombre;

}
