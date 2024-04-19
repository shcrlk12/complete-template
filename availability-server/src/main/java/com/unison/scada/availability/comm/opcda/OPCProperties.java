package com.unison.scada.availability.comm.opcda;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "opc")
@Getter
@Setter
public class OPCProperties {
    private String serverName;
    private String prefixFormat;
}
