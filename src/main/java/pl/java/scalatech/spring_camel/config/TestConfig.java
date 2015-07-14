package pl.java.scalatech.spring_camel.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableAutoConfiguration
@Configuration
@Profile("test")
public class TestConfig {

}
