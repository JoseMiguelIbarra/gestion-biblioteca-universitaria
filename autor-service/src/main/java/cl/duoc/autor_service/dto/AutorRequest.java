package cl.duoc.autor_service.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
public class AutorRequest {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El largo máximo es de 100 caracteres.")
    private String nombre;

    @NotNull(message = "La fecha debe ser formato 'YYYY-MM-DD'.")
    private LocalDate fecha_nac;
}


