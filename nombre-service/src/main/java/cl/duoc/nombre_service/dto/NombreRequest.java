package cl.duoc.nombre_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NombreRequest {

    @NotBlank (message = "El nombre no puede estar en blanco.")
    private String nombre;

    @NotBlank (message = "El segundo nombre no puede estar en blanco.")
    private String segundoNombre;

    @NotBlank (message = "El apellido paterno no puede estar en blanco.")
    private String apPaterno;

    @NotBlank (message = "El apellido materno no puede estar en blanco.")
    private String apMaterno;
}
