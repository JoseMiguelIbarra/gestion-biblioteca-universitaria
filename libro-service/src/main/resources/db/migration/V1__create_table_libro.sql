CREATE TABLE libro(
    id BIGINT PRIMARY KEY auto_increment,
    titulo VARCHAR(100) NOT NULL,
    isbn VARCHAR(100) NOT NULL,
    id_genero BIGINT not null,
    id_autor BIGINT not null,
    estado BOOLEAN not null
);
