package cl.duoc.bibliotecario_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class BibliotecarioResponse {
    private Long id;
    private String nombre;
    private Integer edad;
    private LocalDate fechaIngreso;
}
