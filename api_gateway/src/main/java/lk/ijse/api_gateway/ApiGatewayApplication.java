package lk.ijse.api_gateway;

import lk.ijse.api_gateway.config.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
        return builder.routes()
                .route("user-service-protected", r -> r.path("/user-service/**")
                        .and()
                        .predicate(exchange -> !exchange.getRequest().getURI().getPath().contains("/register")
                                && !exchange.getRequest().getURI().getPath().contains("/login"))
                        .filters(f -> f.filter(jwtFilter))
                        .uri("http://localhost:8081"))

                .build();
    }

}
