package cl.duoc.estante_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estante")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Estante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false)
    private Long numero;

    @Column(name = "id_bodega", nullable = false)
    private Long idBodega;

    @Column(name = "capacidad", nullable = false)
    private Long capacidad;

}
