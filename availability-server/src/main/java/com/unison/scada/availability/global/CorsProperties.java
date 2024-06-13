package com.unison.scada.availability.global;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "unlock-cors")
@Getter
@Setter
public class CorsProperties {
    private String ip;
    private String port;
    private String domain;
}
