package cl.duoc.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NombreResponse {
    private Long id;
    private String nombre;
    private String segundoNombre;
    private String apPaterno;
    private String apMaterno;
}
