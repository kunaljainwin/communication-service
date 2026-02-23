package com.my.service.comms_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "comms")
public class CommsConfig {

    private Exchange exchange = new Exchange();
    private Queue queue = new Queue();
    private Key key = new Key();

    @Data
    public static class Exchange {
        private String type;
        private String name;
    }

    @Data
    public static class Queue {
        private String sms;
        private String email;
        private String notification;
    }

    @Data
    public static class Key {
        private String sms;
        private String email;
        private String notification;
    }

}
