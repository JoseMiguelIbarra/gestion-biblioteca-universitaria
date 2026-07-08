package cl.duoc.estante_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstanteRequest {

    @NotNull(message = "El número no puede ser nulo.")
    private Long numero;

    @NotNull(message = "El id de la bodega no puede ser nulo.")
    private Long idBodega;

    @NotNull(message = "La capacidad no puede ser nula.")
    private Long capacidad;

}
