package com.my.service.comms_service.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TenantHeaderSwaggerConfig {
    @Bean
    public OpenAPI tenantHeaderOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("Header",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("x-tenant-id")
                        .description("Tenant identifier (UUID)")
                )
            )
            .addSecurityItem(new SecurityRequirement().addList("Header"));
    }
}
