package com.nightlifeexplorer.beckend.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nightlife Explorer API")
                        .version("1.0")
                        .description("API per la gestione degli eventi notturni a Napoli"));
    }
}