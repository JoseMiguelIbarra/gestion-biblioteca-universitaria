package cl.duoc.domicilio_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DomicilioRequest {

    @NotBlank(message = "La direccion de la comuna no puede estar vacio")
    private String direccion;

    @NotBlank(message = "La comuna no puede estar vacia")
    private String comuna;

    @NotBlank(message = "El pais no puede estar vacío")
    private String pais;

    

}
