package com.nfdtech.clinic_admin_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinic Admin API")
                        .version("1.0.0")
                        .description("Sistema de Gestão de Clínicas de Especialidades Médicas")
                        .contact(new Contact()
                                .name("NFDTech Development Team")
                                .email("contato@nfdtech.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082" + contextPath)
                                .description("Servidor de Desenvolvimento")
                ));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("com.nfdtech.clinic_admin_api.controllers")
                .addOpenApiCustomizer(openApiCustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // Remove schemas de entidades JPA que não são DTOs
            if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null) {
                openApi.getComponents().getSchemas().entrySet()
                    .removeIf(entry -> {
                        String key = entry.getKey();
                        // Manter apenas schemas de DTOs ou classes do pacote dto
                        return !key.endsWith("DTO") &&
                               !key.equals("Role") &&
                               !key.equals("ErrorResponse") &&
                               !key.equals("ValidationError");
                    });
            }
        };
    }
}
