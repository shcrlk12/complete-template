package com.unison.scada.availability.domain.windfarm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "wind-farm")
@Getter
@Setter
public class WindFarmProperties {

    private int turbinesNumber = 1;
}
