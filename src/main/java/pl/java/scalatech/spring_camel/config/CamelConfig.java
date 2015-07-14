package pl.java.scalatech.spring_camel.config;

import java.util.Random;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.CamelContext;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

@Configuration
@Slf4j
@ComponentScan(basePackages = "pl.java.scalatech.spring_camel.route", includeFilters = { @Filter(Component.class) })
@PropertySource("classpath:camel.properties")
public class CamelConfig {

    @Autowired
    private CamelContext camelContext;

    @PostConstruct
    public void init() {
        camelContext.setTracing(true);
    }

    private static Random random = new Random();
    private static HashFunction hf = Hashing.md5();

    @Bean
    public ServletRegistrationBean soapServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CXFServlet(), "/services/*");
        registration.setName("CXFServlet");
        return registration;
    }

    @Bean
    Bus cxf() {
        return BusFactory.newInstance().createBus();
    }

    @Bean
    @Scope(value = "prototype")
    public String mySimpleBean() {
        return "my hash: " + Hashing.sha256().newHasher().putInt(random.nextInt(100)).hash().toString();
    }

}
