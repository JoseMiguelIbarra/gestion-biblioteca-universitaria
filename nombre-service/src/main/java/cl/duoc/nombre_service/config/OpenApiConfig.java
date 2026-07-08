package cl.duoc.nombre_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nombreServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nombre Service API")
                        .version("1.0.0")
                        .description("Microservicio para la gestion de nombres de la "
                                + "Biblioteca Universitaria. Forma parte de la arquitectura "
                                + "de microservicios registrada en Eureka."));
    }
}