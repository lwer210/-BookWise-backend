package com.project.bookwise.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    public Info info(){
        return new Info()
                .version("1.0")
                .title("BookWise 독서 관리 Project API Swagger")
                .description("BookWise 독서 관리 프로젝트 Swagger");
    }
}
