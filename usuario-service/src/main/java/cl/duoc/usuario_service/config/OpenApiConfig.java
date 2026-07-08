package cl.duoc.usuario_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usuarioServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Usuario Service API")
                        .version("1.0.0")
                        .description("Microservicio para la gestion de usuarios de la Biblioteca Universitaria. "
                                + "Permite registrar, consultar, actualizar y eliminar usuarios, "
                                + "ademas de consultar usuarios asociados a nombres y domicilios. "
                                + "Forma parte de la arquitectura de microservicios registrada en Eureka."));
    }
}