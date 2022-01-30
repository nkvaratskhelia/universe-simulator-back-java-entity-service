package com.example.universe.simulator.entityservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@Validated
@Getter @Setter
public class AppProperties {

    @Valid
    private SchedulingProperties scheduling = new SchedulingProperties();

    /**
     * Logstash connection URL, in the form of host:port.
     */
    @NotBlank
    private String logstashUrl;

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
