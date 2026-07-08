package cl.duoc.domicilio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomicilioResponse {
    
    private Long id;
    private String direccion;
    private String comuna;
    private String pais;

}
