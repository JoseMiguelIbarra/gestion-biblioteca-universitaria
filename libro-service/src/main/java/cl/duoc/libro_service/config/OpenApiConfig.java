package cl.duoc.libro_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libroServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Libro Service API")
                        .version("1.0.0")
                        .description("Microservicio para la gestion de libros de la "
                                + "Biblioteca Universitaria. Integra datos de autor y genero "
                                + "consumiendo sus respectivos microservicios, y forma parte "
                                + "de la arquitectura registrada en Eureka."));
    }
}