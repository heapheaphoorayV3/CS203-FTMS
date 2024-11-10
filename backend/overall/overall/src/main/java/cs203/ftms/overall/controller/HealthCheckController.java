package cs203.ftms.overall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/health")
public class HealthCheckController {

    // @Autowired(required = false)
    // private JdbcTemplate jdbcTemplate;

    // @GetMapping
    // public ResponseEntity<Map<String, Object>> healthCheck() {
    //     Map<String, Object> healthStatus = new HashMap<>();
    //     boolean isHealthy = true;
        
    //     // Check application status
    //     healthStatus.put("status", "UP");
    //     healthStatus.put("timestamp", System.currentTimeMillis());
        
    //     // Check database connection if JdbcTemplate is available
    //     if (jdbcTemplate != null) {
    //         try {
    //             jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    //             healthStatus.put("database", "UP");
    //         } catch (Exception e) {
    //             isHealthy = false;
    //             healthStatus.put("database", "DOWN");
    //             healthStatus.put("database_error", e.getMessage());
    //         }
    //     }
        
    //     // Add memory information
    //     Runtime runtime = Runtime.getRuntime();
    //     Map<String, Long> memory = new HashMap<>();
    //     memory.put("total", runtime.totalMemory());
    //     memory.put("free", runtime.freeMemory());
    //     memory.put("used", runtime.totalMemory() - runtime.freeMemory());
    //     healthStatus.put("memory", memory);
        
    //     // Return 200 if healthy, 503 if unhealthy
    //     return isHealthy 
    //         ? ResponseEntity.ok(healthStatus)
    //         : ResponseEntity.status(503).body(healthStatus);
    // }
    
    // Simplified endpoint for ALB
    @GetMapping("/simple")
    public ResponseEntity<String> simpleHealthCheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}