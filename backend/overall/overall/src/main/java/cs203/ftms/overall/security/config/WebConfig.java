package cs203.ftms.overall.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for handling CORS (Cross-Origin Resource Sharing) globally
 * within the Spring MVC application. Allows customization of origins, methods, and headers.
 */
@Configuration
public class WebConfig {

    /**
     * Configures global CORS settings for the application.
     *
     * @return a WebMvcConfigurer that defines CORS mappings for all endpoints.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Adds CORS mappings to the application, allowing specific HTTP methods,
             * origins, and headers across all endpoints.
             *
             * @param registry the CorsRegistry to configure allowed origins, methods, and headers.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Applies to all endpoints
                        .allowedOrigins("*")  // Allowed origin(s)
                        .allowedMethods("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD")  // Allowed HTTP methods
                        .allowedHeaders("*");  // Allow all headers
                        // .allowCredentials(true);  // Uncomment to allow cookies, authorization headers, etc.
            }
        };
    }
}
