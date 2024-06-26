package com.unison.scada.availability.comm.opcda;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Configuration
@ConfigurationProperties(prefix = "opc")
@Getter
@Setter
public class OPCProperties {
    private String serverName;
    private String prefixFormat;
}
