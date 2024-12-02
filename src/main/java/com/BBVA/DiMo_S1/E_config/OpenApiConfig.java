package com.BBVA.DiMo_S1.E_config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    private static final String SWAGGER_URL = "/swagger-ui.html";


    @SuppressWarnings("null")
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", SWAGGER_URL);
        registry.addRedirectViewController("/swagger-ui", SWAGGER_URL);
        registry.addRedirectViewController("/swagger", SWAGGER_URL);
    }


    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Billetera Virtual - DiMo")
                        .version("1.0.0")
                        .description(
                                "Documentación correspondiente a la aplicación DiMo. " +
                                "La misma simula el funcionamiento de una billetera virtual. Incluye endpoints " +
                                "tanto para acciones como Usuario como para Administrador. La aplicación presenta una " +
                                "integración con Spring Security mediante la generación de Tokens de autenticación."
                                + "\n\nFuncionalidades destacadas:" + "\n\n"
                                        + "\n\n- La aplicación permite la creación de una cuenta bancaria en ARS y USD" +
                                        "\n\n- La aplicación permite realizar depositos y transferencias a distintas cuentas" +
                                        "\n\n- La aplicación permite crear un Plazo Fijo" +
                                        "\n\n- La aplicación permite simular Préstamos"
                                        + "\n\n" +
                                        "Repositorio de GitHub: " +
                                        "[BBVA-FS-W5-Back-S1](https://github.com/alkemyTech/BBVA-FS-W5-Back-S1)"
                        ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme()));
    }
}