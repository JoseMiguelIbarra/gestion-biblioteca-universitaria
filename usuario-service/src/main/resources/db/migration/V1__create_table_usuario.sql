CREATE TABLE usuario(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_nombre BIGINT not null,
    rut VARCHAR(10) NOT NULL,
    edad BIGINT NOT NULL,
    id_domicilio BIGINT NOT NULL,
    fecha_nacimiento DATE NOT NULL
);