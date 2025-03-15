package kma.ktlt.post.app.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.title}") String title ,
            @Value("${open.api.version}") String version ,
            @Value("${open.api.description}") String description ,
            @Value("${open.api.serverUrl}") String serverUrl,
            @Value("${open.api.serverdescription}") String serverdescription,
            @Value("${open.api.license}") String licenseName,
            @Value("${open.api.licenseurl}") String licenseurl
    ){
        return new OpenAPI().info(new Info()
                .title(title)
                .version(version)
                .description(description)

                .license(new License().name(licenseName).url(licenseurl)))
                .servers(List.of(new Server().url(serverUrl).description(serverdescription)))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }


    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service-identity")
                .packagesToScan(
                        "kma.ktlt.post.app.api",
                        "KMA.BeBookingApp.app.api.homestay",
                        "KMA.BeBookingApp.app.api.homestay",
                        "KMA.BeBookingApp.app.api.homestay",
                        "KMA.BeBookingApp.app.api.booking")
                .build();
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CNPM Service API").version("1.0"))
                .addServersItem(new Server().url("/"));
    }
}