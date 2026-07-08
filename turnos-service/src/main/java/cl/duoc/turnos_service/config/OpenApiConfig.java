package cl.duoc.turnos_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI turnoServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Turnos Service API")
                        .version("1.0.0")
                        .description("Microservicio para la gestion de turnos de bibliotecarios de la "
                                + "Biblioteca Universitaria. Integra datos del bibliotecario asociado "
                                + "consumiendo bibliotecario-service, y forma parte de la arquitectura "
                                + "registrada en Eureka."));
    }
}