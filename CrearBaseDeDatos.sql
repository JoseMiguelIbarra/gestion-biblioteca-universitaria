-- Script de inicialización de Bases de Datos
-- José Ibarra - Erick Herrera

-- Crear Autores --
DROP DATABASE IF EXISTS autores;
CREATE DATABASE autores;
USE autores;
CREATE TABLE autor(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(100) NOT NULL,
	fecha_nac DATE NOT NULL
);
INSERT INTO autor (nombre, fecha_nac)
VALUES ('Pablo Neruda', '1904-07-12'), ('Isabel Allende','1942-08-02')
, ('Gabriela Mistral', '1889-04-07');
SELECT * FROM autor;

-- Crear Generos --
DROP DATABASE IF EXISTS generos;
CREATE DATABASE generos;
USE generos;
CREATE TABLE genero (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL
);
INSERT INTO genero (nombre)
VALUES ('Romance'), ('Aventura'), ('Misterio'), ('Fantástico');
SELECT * FROM genero;

-- Crear Libros --
DROP DATABASE IF EXISTS libros;
CREATE DATABASE libros;
USE libros;
CREATE TABLE libro(
    id BIGINT PRIMARY KEY auto_increment,
    titulo VARCHAR(100) NOT NULL,
    isbn VARCHAR(100) NOT NULL,
    id_genero BIGINT not null,
    id_autor BIGINT not null,
    estado BOOLEAN not null
);
INSERT INTO libro (titulo, isbn, id_genero, id_autor, estado)
VALUES ('Caida de las Estrellas', '123-456-789', 1, 1, TRUE),
       ('Amanecer de Plutón', '987-654-321', 2, 2, FALSE),
       ('Anochecer del Día', '123-123-123', 3, 3, TRUE);
SELECT * FROM libro;

-- Crear Nombres --
DROP DATABASE IF EXISTS nombres;
CREATE DATABASE nombres;
USE nombres;
CREATE TABLE nombre (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    segundo_nombre VARCHAR(30) NOT NULL,
    apellido_materno VARCHAR(30) NOT NULL,
    apellido_paterno VARCHAR(30) NOT NULL
);
INSERT INTO nombre (nombre, segundo_nombre, apellido_paterno, apellido_materno)
VALUES ('Bayron', 'Alexander', 'Urrutia', 'Flores'), ('Jose', 'Miguel', 'Ibarra', 'Vyhmeister'),
('Jose', 'Esperanza', 'Calderon', 'Becerra');
SELECT * FROM nombre;

-- Crear Domicilios --
DROP DATABASE IF EXISTS domicilios;
CREATE DATABASE domicilios;
USE domicilios;
CREATE TABLE domicilio(

    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    direccion VARCHAR(50) NOT NULL,
    comuna VARCHAR(50) NOT NULL,
    pais VARCHAR(50) NOT NULL
);
INSERT INTO domicilio(direccion, comuna, pais)
VALUES('Av. Eyzaguirre 450','San Bernardo','Chile'),
('Av. Concha y Toro 2840','Puente Alto', 'Chile'),
('Av. Santa Rosa 12950', 'La Pintana', 'Chile');
SELECT * FROM domicilio;

-- Crear Usuarios --
DROP DATABASE IF EXISTS usuarios;
CREATE DATABASE usuarios;
USE usuarios;
CREATE TABLE usuario(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_nombre BIGINT not null,
    rut VARCHAR(10) NOT NULL,
    edad BIGINT NOT NULL,
    id_domicilio BIGINT NOT NULL,
    fecha_nacimiento DATE NOT NULL
);
INSERT INTO usuario (id_nombre, rut, edad, id_domicilio, fecha_nacimiento)
VALUES (1, '22190612-8', 19, 1, '2006-08-22')
     , (2, '24185940-4', 27, 2, '1999-06-12')
     , (3, '47284758-2', 23, 3, '2001-04-23');
SELECT * FROM usuario;

-- Crear Bodegas --
DROP DATABASE IF EXISTS bodegas;
CREATE DATABASE bodegas;
USE bodegas;
CREATE TABLE bodega(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
	ubicacion VARCHAR(100) NOT NULL,
 	capacidad BIGINT NOT NULL
);
INSERT INTO bodega (nombre, ubicacion, capacidad)
VALUES
    ('Bodega Principal', 'Piso 1', 500),
    ('Bodega Secundaria', 'Piso 2', 300),
    ('Bodega Archivo', 'Sotano', 800),
    ('Bodega Temporal', 'Piso 3', 150);
SELECT * FROM bodega;

-- Crear Estantes --
DROP DATABASE IF EXISTS estantes;
CREATE DATABASE estantes;
USE estantes;
CREATE TABLE estante(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero BIGINT NOT NULL,
    id_bodega BIGINT NOT NULL,
    capacidad BIGINT NOT NULL
);
INSERT INTO estante (numero, id_bodega, capacidad)
VALUES  (1, 1, 900),
        (2, 2, 1500),
        (3, 3, 2000);
SELECT * FROM estante;

-- Crear Bibliotecarios--
DROP DATABASE IF EXISTS bibliotecario;
CREATE DATABASE bibliotecario;
USE bibliotecario;
CREATE TABLE bibliotecario(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    edad INT NOT NULL,
    fecha_ingreso DATE NOT NULL
);
INSERT INTO bibliotecario (nombre, edad, fecha_ingreso) VALUES
	('Carlos Méndez',     34, '2020-03-15'),
	('Laura Sepúlveda',   28, '2022-07-01'),
	('Andrés Villanueva', 45, '2018-11-20');
SELECT * FROM bibliotecario;

-- Crear Turnos --
DROP DATABASE IF EXISTS turnos;
CREATE DATABASE  turnos;
USE turnos;

CREATE TABLE turno (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_bibliotecario BIGINT NOT NULL,
    fecha_turno     DATE NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,
    tipo_turno      VARCHAR(20) NOT NULL
);

INSERT INTO turno (id_bibliotecario, fecha_turno, hora_inicio, hora_fin, tipo_turno)
VALUES
    (1, '2026-06-10', '08:00:00', '14:00:00', 'MANANA'),
    (2, '2026-06-10', '14:00:00', '20:00:00', 'TARDE'),
    (3, '2026-06-11', '08:00:00', '14:00:00', 'MANANA');
SELECT * FROM turno;
