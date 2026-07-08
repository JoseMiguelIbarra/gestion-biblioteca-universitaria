package cl.duoc.usuario_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    private Long id;
    private NombreResponse nombre;
    private String rut;
    private Long edad;
    private DomicilioResponse domicilio;
    private LocalDate fechaNacimiento;
}
