package com.example.universe.simulator.entityservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "app.scheduling", ignoreUnknownFields = false)
@Getter @Setter
@Validated
class SchedulingProperties {

    /**
     * Enables app-wide scheduling.
     */
    private boolean enabled = true;

    /**
     * SpaceEntityStatisticsJob cron expression.
     */
    @NotNull
    private String spaceEntityStatisticsJobCron;
}
