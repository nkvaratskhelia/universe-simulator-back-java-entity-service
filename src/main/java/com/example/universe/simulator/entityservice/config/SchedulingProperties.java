package com.example.universe.simulator.entityservice.config;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.scheduling")
@Validated
@AllArgsConstructor
@Getter
class SchedulingProperties {

    /**
     * Enables app-wide scheduling.
     */
    private boolean enabled;

    /**
     * SpaceEntityStatisticsJob cron expression.
     */
    @NotBlank
    private String spaceEntityStatisticsJobCron;
}
