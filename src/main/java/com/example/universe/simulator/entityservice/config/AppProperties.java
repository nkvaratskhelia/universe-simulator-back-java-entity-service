package com.example.universe.simulator.entityservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app")
@Validated
@Getter @Setter
public class AppProperties {

    @Valid
    private SchedulingProperties scheduling = new SchedulingProperties();

    @Getter @Setter
    public static class SchedulingProperties {

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
}
