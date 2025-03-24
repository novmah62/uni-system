package com.drewsec.user_central.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        authorizationUrl = "http://localhost:8080/realms/uni-system/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:8080/realms/uni-system/protocol/openid-connect/token"
                )
        )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.title}") String title ,
            @Value("${open.api.version}") String version ,
            @Value("${open.api.description}") String description ,
            @Value("${open.api.serverUrl}") String serverUrl,
            @Value("${open.api.serverDescription}") String serverDescription,
            @Value("${open.api.license}") String licenseName,
            @Value("${open.api.licenseUrl}") String licenseUrl
    ){
        return new OpenAPI().info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)

                        .license(new License().name(licenseName).url(licenseUrl)))
                .servers(List.of(new Server().url(serverUrl).description(serverDescription)));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("User Central API").version("1.0"))
                .addServersItem(new Server().url("/"));
    }

}
