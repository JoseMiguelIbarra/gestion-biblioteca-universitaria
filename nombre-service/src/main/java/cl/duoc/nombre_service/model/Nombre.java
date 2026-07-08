package cl.duoc.nombre_service.model;

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
@Table(name = "nombre")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Nombre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre",nullable = false)
    private String nombre;

    @Column(name = "segundo_nombre",nullable = false)
    private String segundoNombre;

    @Column(name = "apellido_paterno",nullable = false)
    private String apPaterno;

    @Column(name = "apellido_materno",nullable = false)
    private String apMaterno;

}
