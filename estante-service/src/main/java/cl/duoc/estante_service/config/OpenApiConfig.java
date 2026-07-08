package cl.duoc.estante_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI estanteServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Estante Service API")
                        .version("1.0.0")
                        .description("Microservicio para la gestion de estantes de la "
                                + "Biblioteca Universitaria. Integra datos de la bodega "
                                + "asociada consumiendo bodega-service, y forma parte de la "
                                + "arquitectura registrada en Eureka."));
    }
}