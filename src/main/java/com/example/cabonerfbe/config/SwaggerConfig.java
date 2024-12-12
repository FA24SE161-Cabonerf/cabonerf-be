package com.example.cabonerfbe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The class Swagger config.
 *
 * @author SonPHH.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Public api method.
     *
     * @return the grouped open api
     */
    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }

    /**
     * Cabonerf api method.
     *
     * @return the open api
     */
    @Bean
    OpenAPI cabonerfApi() {
        return new OpenAPI()
                .info(new Info().title("Cabonerf API").version("1.0"))
                .components(new Components()
                        .addSecuritySchemes("x-user-id", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("x-user-id"))
                        .addSecuritySchemes("x-user-role", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("x-user-role"))
                        .addSecuritySchemes("x-user-active", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("x-user-active"))
                        .addSecuritySchemes("gatewayToken", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("gatewayToken")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("x-user-id")
                        .addList("x-user-role")
                        .addList("x-user-active")
                        .addList("gatewayToken"));
    }
}
