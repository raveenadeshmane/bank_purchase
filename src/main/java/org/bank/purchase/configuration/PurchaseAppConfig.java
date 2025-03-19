package org.bank.purchase.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class PurchaseAppConfig {

    @Bean
    public OpenAPI purchaseApprovalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CQRS Purchase System API")
                        .version("1.0.0"));

    }

    @Bean
    public GroupedOpenApi purchaseApiGroup() {
        return GroupedOpenApi.builder()
                .group("Purchase_Approval_System")
                .pathsToMatch("/api/purchase/**")
                .packagesToScan("org.bank.purchase.controller")
                .build();
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/purchase/approval",
                                "/",
                                "/index.html",
                                "/static/**",
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/purchase/approval")
                )
                .cors(withDefaults());

        return http.build();
    }
}