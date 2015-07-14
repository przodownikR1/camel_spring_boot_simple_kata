package pl.java.scalatech.spring_camel.service;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Consume;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerOne {
    @Consume(uri = "seda:finish")
    public void consumeMessage(String message) {
        log.info("+++        {}", message);
    }

}
