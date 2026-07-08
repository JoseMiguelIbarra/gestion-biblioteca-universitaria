package cl.duoc.bodega_service.dto;

import jakarta.validation.constraints.NotBlank;
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
public class BodegaRequest {
    @NotBlank(message = "El nombre de la bodega no puede estar vacío.")
    @Size(max = 100, message = "El largo máximo es de 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El nombre de la ubicación no puede estar vacío.")
    @Size(max = 100, message = "El largo máximo es de 100 caracteres.")
    private String ubicacion;
    
    @NotNull(message = "La capacidad no puede ser nulo.")
    private Long capacidad;
}
