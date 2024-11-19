package cs203.ftms.overall.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration class for application security settings, including CORS, session management,
 * JWT authentication, and request authorization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructs a new SecurityConfiguration.
     *
     * @param jwtAuthenticationFilter Filter for handling JWT authentication.
     * @param authenticationProvider  Authentication provider for validating user credentials.
     */
    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the HTTP security settings for the application.
     * - Enables CORS with a custom configuration.
     * - Disables CSRF protection (for stateless APIs).
     * - Sets session management to stateless for JWT-based authentication.
     * - Adds a JWT authentication filter before the UsernamePasswordAuthenticationFilter.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs while building the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/fencer/women*").permitAll()
                        .requestMatchers("/api/v1/fencer/men*").permitAll()
                        .requestMatchers("/api/v1/tournament/tournaments").permitAll()
                        .requestMatchers("/api/v1/tournament/tournament-details/**").permitAll()
                        .requestMatchers("/api/v1/event/event-details/**").permitAll()
                        .requestMatchers("/api/v1/event/get-event-ranking/**").permitAll()
                        .requestMatchers("/api/v1/poule/get-poule-table/**").permitAll()
                        .requestMatchers("/api/v1/direct-elimination/get-direct-elimination-matches/**").permitAll()
                        .requestMatchers("/health/simple").permitAll()
                        .requestMatchers("/api/v1/poule/get-poules-result/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configures CORS settings to allow specified origins, methods, and headers.
     *
     * @return The CorsConfigurationSource with the CORS configuration.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
