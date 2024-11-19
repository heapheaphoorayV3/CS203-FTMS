package cs203.ftms.overall.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.Executor;

/**
 * Configuration class for enabling asynchronous processing in the application.
 * Defines a custom thread pool executor for handling mail-related tasks.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    
    /**
     * Creates a custom thread pool executor for mail-related tasks.
     * This executor is configured with specific thread pool settings to handle asynchronous operations efficiently.
     *
     * @return an Executor configured for mail processing
     */
    @Bean(name = "mailExecutor")
    public Executor mailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Minimum number of threads in the pool
        executor.setMaxPoolSize(4); // Maximum number of threads in the pool
        executor.setQueueCapacity(50); // Capacity of the task queue
        executor.setThreadNamePrefix("MailThread-"); // Prefix for thread names
        executor.initialize();
        return executor;
    }
}
