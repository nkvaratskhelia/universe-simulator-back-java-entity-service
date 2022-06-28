package com.example.universe.simulator.entityservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app.scheduling", ignoreInvalidFields = true)
@Component
@Validated
@Getter @Setter
public class SchedulingProperties {

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
