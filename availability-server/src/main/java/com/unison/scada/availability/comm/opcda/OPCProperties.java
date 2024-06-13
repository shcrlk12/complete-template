package com.unison.scada.availability.comm.opcda;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
@ConfigurationProperties(prefix = "opc")
@Getter
@Setter
public class OPCProperties {
    private String serverName;
    private String prefixFormat;

    private List<Variable> variable;

    @Getter
    @Setter
    public static class Variable{
        private String name;
        private String type;
    }
}
