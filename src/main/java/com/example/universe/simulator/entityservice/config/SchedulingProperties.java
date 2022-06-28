package com.example.universe.simulator.entityservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app.scheduling")
@ConstructorBinding
@Validated
@AllArgsConstructor
@Getter
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
