package cl.duoc.bodega_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BodegaResponse {
    private Long id;
    private String nombre;
    private String ubicacion;
    private Long capacidad;
}
