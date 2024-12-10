package com.bmi.bds.testwebflux;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private static final double MEMORY_THRESHOLD_PERCENTAGE = 80.0; // Threshold for memory usage

    @Override
    public Health health() {
        boolean serviceUp = checkServiceHealth();
        MemoryStatus memoryStatus = checkMemoryHealth();

        if (serviceUp && memoryStatus.healthy()) {
            return Health.up()
                    .withDetail("service", "Available")
                    .withDetail("memoryUsedPercentage", memoryStatus.usedPercentage())
                    .withDetail("memoryStatus", "Healthy")
                    .build();
        } else {
            return Health.down()
                    .withDetail("service", serviceUp ? "Available" : "Unavailable")
                    .withDetail("memoryUsedPercentage", memoryStatus.usedPercentage())
                    .withDetail("memoryStatus", memoryStatus.healthy() ? "Healthy" : "Critical")
                    .build();
        }
    }

    private boolean checkServiceHealth() {
        return true;
    }

    private MemoryStatus checkMemoryHealth() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        double usedMemory = totalMemory - freeMemory;
        double usedPercentage = (usedMemory / totalMemory) * 100;

        boolean isHealthy = usedPercentage < MEMORY_THRESHOLD_PERCENTAGE;
        return new MemoryStatus(usedPercentage, isHealthy);
    }

    private record MemoryStatus(double usedPercentage, boolean healthy) {
    }
}
