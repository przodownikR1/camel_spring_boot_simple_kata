package pl.java.scalatech.spring_camel.config;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
@ComponentScan(basePackages = "pl.java.scalatech.spring_camel.route", includeFilters = { @Filter(Component.class) })
public class CamelConfig {
    @Autowired
    private CamelContext camelContext;

}
