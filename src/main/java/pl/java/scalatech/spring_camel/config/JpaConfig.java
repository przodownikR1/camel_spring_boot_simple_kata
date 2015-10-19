package pl.java.scalatech.spring_camel.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "pl.java.scalatech.spring_camel.repository")
@EntityScan(basePackages = "pl.java.scalatech.spring_camel.domain")
@Slf4j
public class JpaConfig {

}
