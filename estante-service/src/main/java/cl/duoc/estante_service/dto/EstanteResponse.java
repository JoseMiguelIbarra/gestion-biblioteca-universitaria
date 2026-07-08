package cl.duoc.estante_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstanteResponse {
    private Long id;
    private Long numero;
    private BodegaResponse bodega;
    private Long capacidad;
}
