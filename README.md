# Gestión de Biblioteca Universitaria
Sistema de gestión para biblioteca digital basada en microservicios.

#Descripción del proyecto

Sistema de gestión para una biblioteca universitaria, construido bajo una
arquitectura de microservicios independientes que se comunican mediante REST.
Cada microservicio gestiona una entidad acotada del dominio (autores, libros,
géneros, usuarios, bibliotecarios, turnos, etc.), siguiendo el patrón
CSR (Controller–Service–Repository/Model) con separación real de
responsabilidades por capa.

Los servicios se registran en un servidor Eureka para el descubrimiento
dinámico, y un API Gateway centraliza el enrutamiento de todas las
solicitudes hacia los microservicios. Algunos servicios consumen a otros de
forma remota (por ejemplo, libro-service obtiene los datos de autor y género
desde sus respectivos microservicios) usando WebClient.

#Integrantes del equipo.

José Miguel Ibarra.
Erick Herrera.

## Requisitos Previos
Asegurarse de tener instalado lo siguiente antes de ejecutar el proyecto:
- Java 21
- MySQL 8+
- IDE recomendado: IntelliJ IDEA o Visual Studio Code

## Tecnologías
| Dependencia         | Uso                                        |
| ------------------- | ------------------------------------------ |
| Lombok              | Reducción de código repetitivo.            |
| Spring Web          | Endpoints REST y controladores.            |
| Spring Data JPA     | Persistencia y operaciones CRUD.           |
| Validation          | Validación de datos (Bean Validation).     |
| MySQL Driver        | Conexión con base de datos MySQL.          |
| Flyway Migration    | Migraciones y versionado de base de datos. |
| Spring Reactive Web | Comunicación entre microservicios.         |

## Microservicios
| Microservicio              | Descripción                                                               | Puerto |
| -------------------------- | ------------------------------------------------------------------------- | ------ |
| 1. autor-service           | Encargado de gestionar los autores de los libros.                         | 8081   |
| 2. genero-service          | Encargado de gestionar los generos de los libros.                         | 8082   |
| 3. libro-service           | Encargado de gestionar libros y comunicar autores y generos.              | 8083   |
| 4. nombre-service          | Encargado de gestionar los nombres de los usuarios.                       | 8084   |
| 5. domicilio-service       | Encargado de gestionar los domicilios de los usuarios.                    | 8085   |
| 6. usuario-service         | Encargado de gestionar los usuarios y comunicar nombres y domicilios.     | 8086   |
| 7. bibliotecario-service   | Encargado de gestionar los bibliotecarios                                 | 8087   |
| 8. turno-service           | Encargado de gestionar los turnos y comunicarse con bibliotecario.        | 8088   |
| 9. bodega-service          | Encargado de gestionar las bodegas de los estantes.                       | 8089   |
| 10. estante-service        | Encargado de gestionar los estantes y comunicar bodegas.                  | 8090   |
| 11. gateway-service        | API Gateway: enrutamiento centralizado                                    | 8080   |

#Rutas principales del Gateway


Ruta del Gateway                    Microservicio destino

/autores/**                         autor-service
/generos/**                         genero-service
/libros/**                          libro-service
/nombres/**                         nombre-service
/domicilios/**                      domicilio-service
/usuarios/**                        usuario-service
/bibliotecarios/**                  bibliotecario-service
/turnos/**                          turnos-service
/bodegas/**                         bodega-service
/estantes/**                        estante-service


#Documentación Swagger / OpenAPI


Microservicio            Enlace Swagger

autor-service             http://localhost:8081/swagger-ui.html
genero-service            http://localhost:8082/swagger-ui.html
libro-service             http://localhost:8083/swagger-ui.html
nombre-service            http://localhost:8084/swagger-ui.html
domicilio-service         http://localhost:8085/swagger-ui.html
usuario-service           http://localhost:8086/swagger-ui.html
bibliotecario-service     http://localhost:8087/swagger-ui.html
turnos-service            http://localhost:8088/swagger-ui.html
bodega-service            http://localhost:8089/swagger-ui.html
estante-service           http://localhost:8090/swagger-ui.html

#Ejecución local

1. Creacion de base de datos.

Crear bases de datos.
CREATE DATABASE autor;
CREATE DATABASE bibliotecario;
CREATE DATABASE bodegas;
CREATE DATABASE domicilios;
CREATE DATABASE estantes;
CREATE DATABASE generos;
CREATE DATABASE libros;
CREATE DATABASE nombres;
CREATE DATABASE turnos;
CREATE DATABASE usuarios;

2. Orden de arranque

1- MySQL (debe estar arriba antes que todo).
2- eureka (servidor de registro, puerto 8761).
3- Microservicios (autor, genero, libro, etc.), en cualquier orden.
4- gateway-service (al final).


## Funcionalidades Implementadas
* Funcionalidad 1: Buscar todos.
* Funcionalidad 2: Buscar por Id.
* Funcionalidad 3: Crear.
* Funcionalidad 4: Actualizar.
* Funcionalidad 5: Eliminar por Id.

## Pasos para ejecutar

1. Clonar el repositorio git clone https://github.com/JoseMiguelIbarra/Gestion-Biblioteca-Universitaria-main.git
2. Ejecutar el script "CrearBaseDeDatos.sql" en MySQL para comenzar el creado y poblado de tablas y bases de datos.
3. Configurar las credenciales en el "application.properties" de cada microservicio:
- spring.datasource.url=jdbc:mysql://localhost:3306/[nombre]
- spring.datasource.username=root
- spring.datasource.password=[contraseña]
4. Ejecutar comando docker compose up --build -d.
5. Verificar que cada microservicio esté corriendo en Eureka, http://localhost:8761/.


## Endpoints

### 1. autor-service
| Método   | Ruta            | Descripción                  |
|----------|-----------------|------------------------------|
| GET      | /autores        | Obtiene todos los autores    |
| GET      | /autores/{id}   | Obtiene un autor por su ID   |
| POST     | /autores        | Crea un nuevo autor          |
| PUT      | /autores/{id}   | Actualiza un autor por su ID |
| DELETE   | /autores/{id}   | Elimina un autor por su ID   |

### 2. generos-service
| Método   | Ruta            | Descripción                   |
|----------|-----------------|-------------------------------|
| GET      | /generos        | Obtiene todos los generos     |
| GET      | /generos/{id}   | Obtiene un genero por su ID   |
| POST     | /generos        | Crea un nuevo genero          |
| PUT      | /generos/{id}   | Actualiza un genero por su ID |
| DELETE   | /generos/{id}   | Elimina un genero por su ID   |

### 3. libro-service
| Método   | Ruta                       | Descripción                             |
|----------|----------------------------|-----------------------------------------|
| GET      | /libros                    | Obtiene todos los libros                |
| GET      | /libros/{id}               | Obtiene un libro por su ID              |
| GET      | /libros/autor/{idAutor}    | Obtiene un libro por el ID de su autor  |
| GET      | /libros/genero/{idGenero}  | Obtiene un libro por el ID de su genero |
| POST     | /libros                    | Crea un nuevo libro                     |
| PUT      | /libros/{id}               | Actualiza un libro por su ID            |
| DELETE   | /libros/{id}               | Elimina un libro por su ID              |

### 4. nombre-service
| Método   | Ruta            | Descripción                   |
|----------|-----------------|-------------------------------|
| GET      | /nombres        | Obtiene todos los nombres     |
| GET      | /nombres/{id}   | Obtiene un nombre por su ID   |
| POST     | /nombres        | Crea un nuevo nombre          |
| PUT      | /nombres/{id}   | Actualiza un nombre por su ID |
| DELETE   | /nombres/{id}   | Elimina un nombre por su ID   |

### 5. domicilio-service
| Método   | Ruta               | Descripción                      |
|----------|--------------------|----------------------------------|
| GET      | /domicilios        | Obtiene todos los domicilios     |
| GET      | /domicilios/{id}   | Obtiene un domicilio por su ID   |
| POST     | /domicilios        | Crea un nuevo domicilio          |
| PUT      | /domicilios/{id}   | Actualiza un domicilio por su ID |
| DELETE   | /domicilios/{id}   | Elimina un domicilio por su ID   |

### 6. usuario-service
| Método   | Ruta                              | Descripción                                  |
|----------|-----------------------------------|----------------------------------------------|
| GET      | /usuarios                         | Obtiene todos los usuarios                   |
| GET      | /usuarios/{id}                    | Obtiene un usuario por su ID                 |
| GET      | /usuarios/nombre/{idNombre}       | Obtiene un usuario por el ID de su nombre    |
| GET      | /usuarios/domicilio/{idDomicilio} | Obtiene un usuario por el ID de su domicilio |
| POST     | /usuarios                         | Crea un nuevo usuario                        |
| PUT      | /usuarios/{id}                    | Actualiza un usuario por su ID               |
| DELETE   | /usuarios/{id}                    | Elimina un usuario por su ID                 |

### 7. bibliotecario-service
| Método   | Ruta                    | Descripción                              |
|----------|-------------------------|------------------------------------------|
| GET      | /bibliotecarios         | Obtiene todos los bibliotecarios         |
| GET      | /bibliotecarios/{id}    | Obtiene un bibliotecario por su ID       |
| POST     | /bibliotecarios         | Crea un nuevo bibliotecario              |
| PUT      | /bibliotecarios/{id}    | Actualiza un bibliotecario por su ID     |
| DELETE   | /bibliotecarios/{id}    | Elimina un bibliotecario por su ID       |

### 8. turno-service
| Método | Ruta                                    | Descripción                              |
|--------|-----------------------------------------|------------------------------------------|
| GET    | /turnos                                 | Obtiene todos los turnos                 |
| GET    | /turnos/{id}                            | Obtiene un turno por su ID               |
| GET    | /turnos/bibliotecario/{idBibliotecario} | Obtiene los turnos de un bibliotecario |
| GET    | /turnos/fecha/{fecha}                   | Obtiene los turnos de una fecha          |
| GET    | /turnos/tipo/{tipoTurno}                | Obtiene los turnos por tipo              |
| POST   | /turnos                                 | Crea un nuevo turno                      |
| PUT    | /turnos/{id}                            | Actualiza un turno por su ID             |
| DELETE | /turnos/{id}                            | Elimina un turno por su ID               |

### 9. bodega-service
| Método   | Ruta            | Descripción                    |
|----------|-----------------|--------------------------------|
| GET      | /bodegas        | Obtiene todas las bodegas      |
| GET      | /bodegas/{id}   | Obtiene una bodega por su ID   |
| POST     | /bodegas        | Crea una nueva bodega          |
| PUT      | /bodegas/{id}   | Actualiza una bodega por su ID |
| DELETE   | /bodegas/{id}   | Elimina una bodega por su ID   |

### 10. estante-service
| Método   | Ruta                          | Descripción                               |
|----------|-------------------------------|-------------------------------------------|
| GET      | /estantes                     | Obtiene todos los estantes                |
| GET      | /estantes/{id}                | Obtiene un estante por su ID              |
| GET      | /estantes/bodega/{idBodega}   | Obtiene un estante por el ID de la bodega |
| POST     | /estantes                     | Crea un nuevo estante                     |
| PUT      | /estantes/{id}                | Actualiza un estante por su ID            |
| DELETE   | /estantes/{id}                | Elimina un estante por su ID              |


