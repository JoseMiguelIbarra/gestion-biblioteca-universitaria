CREATE TABLE turno (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_bibliotecario BIGINT NOT NULL,
    fecha_turno     DATE NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,
    tipo_turno      VARCHAR(20) NOT NULL
);
