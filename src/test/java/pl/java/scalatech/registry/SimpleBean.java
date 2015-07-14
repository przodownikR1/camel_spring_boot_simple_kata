package pl.java.scalatech.registry;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleBean {
    @Getter
    @Setter
    String value;
    public String hello(String message) {
        log.info("+++  {}", message);
        return "Hello" + message;
    }
}