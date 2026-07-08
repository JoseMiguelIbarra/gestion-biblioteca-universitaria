package cl.duoc.usuario_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequest {

    @NotNull(message = "El id del nombre no puede ser nulo.")
    private Long idNombre;

    @NotEmpty(message = "El rut no puede estar vacío.")
    @Size(max = 10, message = "El rut debe tener formato '12345678-X'.")
    private String rut;

    @NotNull(message = "La edad no puede ser nula.")
    private Long edad;

    @NotNull(message = "El id del domicilio no puede ser nulo.")
    private Long idDomicilio;

    @NotNull(message = "La fecha de nacimiento no puede ser nula.")
    private LocalDate fechaNacimiento;
}
