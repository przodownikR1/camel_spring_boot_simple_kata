package pl.java.scalatech.spring_camel.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CamelJMSConfig {

    @Bean(name = "activeMq")
    public ActiveMQComponent activeMq() {
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConnectionFactory(activeMqConnectionFactory());
        return activeMQComponent;
    }

    @Bean(name = "activeMqConnectionFactory")
    public ConnectionFactory activeMqConnectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    @Bean(name = "jmsConfig")
    @DependsOn(value = { "activeMqConnectionFactory", "jmsTransactionManager" })
    public JmsConfiguration jmsConfiguration() {
        JmsConfiguration jmsConfiguration = new JmsConfiguration(activeMqConnectionFactory());
        jmsConfiguration.setTransacted(true);
        jmsConfiguration.setTransactionManager(jmsTransactionManager());
        jmsConfiguration.setCacheLevelName("CACHE_CONNECTION");
        return jmsConfiguration;
    }

    @Bean(name = "jmsTransactionManager")
    @DependsOn(value = { "activeMqConnectionFactory" })
    public PlatformTransactionManager jmsTransactionManager() {
        JmsTransactionManager transactionManager = new JmsTransactionManager();
        transactionManager.setConnectionFactory(activeMqConnectionFactory());
        return transactionManager;
    }
}