package com.my.service.comms_service.util.config_props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ServiceClientProperties {
    
    private Storage storage;

    @Data
    public static class Storage {
        private String baseUrl;
    }
}
