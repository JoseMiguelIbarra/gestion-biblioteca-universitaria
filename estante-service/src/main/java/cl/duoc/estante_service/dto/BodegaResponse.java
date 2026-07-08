package cl.duoc.estante_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BodegaResponse {
    private Long id;
    private String nombre;
    private String ubicacion;
    private Long capacidad;
}
