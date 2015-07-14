package pl.java.scalatech.spring_camel.config;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.boot.CamelContextConfiguration;
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
public class CamelConfig implements CamelContextConfiguration {

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

    @Bean
    public Supplier<LocalDateTime> currentTime() {
        return () -> LocalDateTime.now();
    }

    @Bean
    RouteBuilder exampleRoute(Supplier<LocalDateTime> currentTime) {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:exampleRoute").setHeader("curTime", method(currentTime)).log("${header[now]} - Hello, ${body}!!");
            }
        };
    }

    @Override
    public void beforeApplicationStart(CamelContext camelContext) {
        log.info("+++  beforeApplicationStart");
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("test-activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

    }
}
