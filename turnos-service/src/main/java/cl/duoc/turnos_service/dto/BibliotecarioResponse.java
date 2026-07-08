package cl.duoc.turnos_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BibliotecarioResponse {

    private Long id;
    private String nombre;
    private Integer edad;
    private LocalDate fechaIngreso;

}
