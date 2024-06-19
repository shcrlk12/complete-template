package com.unison.scada.availability.api.windfarm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wind-farm")
@Getter
@Setter
public class WindFarmProperties {

    private int turbinesNumber = 1;
}
