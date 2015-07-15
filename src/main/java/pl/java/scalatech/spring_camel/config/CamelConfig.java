package pl.java.scalatech.spring_camel.config;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
@ComponentScan(basePackages = { "pl.java.scalatech.spring_camel.route", "pl.java.scalatech.spring_camel.processor" }, includeFilters = { @Filter(Component.class) })
@PropertySource("classpath:camel.properties")
public class CamelConfig implements CamelContextConfiguration {

    @Autowired
    private CamelContext camelContext;

    @PostConstruct
    public void init() {
        camelContext.setTracing(true);
    }

    @Override
    public void beforeApplicationStart(CamelContext camelContext) {
        log.info("+++  beforeApplicationStart");
       // final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
      //  camelContext.addComponent("test-activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

    }

    //TODO
    private void setupActiveMQComponent(CamelContext camelContext) {
        // setup the ActiveMQ component
        log.debug("Setup the ActiveMQ Component");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("vm://localhost?broker.persistent=false&broker.useJmx=false");

        // and register it into the CamelContext
        log.debug("Registering JmsComponenet in camel context");
        JmsComponent answer = new JmsComponent();
        answer.setConnectionFactory(connectionFactory);
        camelContext.addComponent("jms", answer);
    }

}
