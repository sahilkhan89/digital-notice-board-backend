package digital_board.digital_board.Config;

import org.springframework.web.client.RestTemplate;

import digital_board.digital_board.Entity.ExceptionResponse;
import digital_board.digital_board.constants.ResponseMessagesConstants;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@Deprecated
public class SecurityConfig {
    private static final String[] public_urls = {
            "/login",
            "/api/v1/user/public",
            "api/v1/auth/**",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/api/v1/notice/byCategory/**",
            "/api/v1/notice/byDepartment/**",
            "/api/v1/notice/important/**",
            "/api/v1/notice/search/**",
            "/api/v1/notice/get/byNoticeId/**",
            "/api/v1/notice/activeNoticeDepartmentCount",
            "/api/v1/notice/activeNoticeCategoryCount",
            "/api/v1/notice/categories/count",
            "/api/v1/user/FindAllUser",
            "/api/v1/notification/create",
            "/api/v1/notice/getAll/**",
            "/api/v1/user/admin-list",
            "/api/v1/notice/today/created/notice/count",
            
    };


    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://dev-2v6nqrql62h5dwnv.us.auth0.com/.well-known/jwks.json")
                .build();
    }

     
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return ((HttpSecurity) http.cors(withDefaults())).csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers(public_urls).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(
                        oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.decoder(jwtDecoder())))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            Map<String, Object> errorDetails = new HashMap<>();
                            errorDetails.put("message", "Unauthorized Access");
                            errorDetails.put("details", ResponseMessagesConstants.messagelist.stream()
                                    .filter(exceptionResponse -> "UNAUTHORIZED_ACCESS"
                                            .equals(exceptionResponse.getExceptonName()))
                                    .map(ExceptionResponse::getMassage)
                                    .findFirst()
                                    .orElse("Default message if not found"));
                            response.getWriter().write("error: " + errorDetails);
                        }))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // @Bean
    // public CorsFilter corsFilter() {
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // CorsConfiguration config = new CorsConfiguration();
    // config.setAllowCredentials(true);
    // config.addAllowedOrigin("*"); // Allow all origins
    // config.addAllowedHeader("*"); // Allow all headers
    // config.addAllowedMethod("OPTIONS");
    // config.addAllowedMethod("GET");
    // config.addAllowedMethod("POST");
    // config.addAllowedMethod("PUT");
    // config.addAllowedMethod("DELETE");
    // source.registerCorsConfiguration("/**", config);
    // return new CorsFilter(source);
    // }
}