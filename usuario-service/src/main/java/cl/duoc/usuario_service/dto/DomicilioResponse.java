package cl.duoc.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomicilioResponse {
    private Long id;
    private String direccion;
    private String comuna;
    private String pais;
}

