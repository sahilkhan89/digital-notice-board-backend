package digital_board.digital_board.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * SwaggerConfig
 * /v3/api-docs
 */
//http://localhost:8080/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

   
    @Bean
    public OpenAPI openAPI(){
        
        String schemeName ="bearerScheme";

        return new OpenAPI()
        .addSecurityItem(new SecurityRequirement()
        .addList(schemeName)
        )
        .components(new Components()
        .addSecuritySchemes(schemeName, new SecurityScheme()
        .name(schemeName)
        .type(SecurityScheme.Type.HTTP)
        .bearerFormat("JWT")
        .scheme("bearer")
        
        )
        )
        .info(new Info()
        .title("Digital Dashboard API")
        .description(("This is digital dashboard Application"))
        .version("1.0")
        .contact(new Contact().name("digital dashboard").email("digitaldashboard@ssism.org").url("digitaldashboard.com"))
        .license(new License().name("Apache"))
        ).externalDocs(new ExternalDocumentation().url(" digitalashboard.com").description("this is external url"));
    }
}