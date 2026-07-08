package cl.duoc.nombre_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NombreResponse {

    private Long id;
    private String nombre;
    private String segundoNombre;
    private String apPaterno;
    private String apMaterno;
}
