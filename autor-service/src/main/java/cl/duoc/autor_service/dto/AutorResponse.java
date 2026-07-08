package cl.duoc.autor_service.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutorResponse {

    private Long id;
    private String nombre;
    private LocalDate fecha_nac;

}
