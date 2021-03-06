package pl.java.scalatech;

import java.math.BigDecimal;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jms.connection.CachingConnectionFactory;

import pl.java.scalatech.spring_camel.beans.Customer;
import pl.java.scalatech.spring_camel.eip_beans.MessageBean;

@Slf4j
public class JmsSendReceiveTest {
    private CamelContext camelContext;
    private ProducerTemplate pt;
    private ConsumerTemplate ct;

    @Test
    public void shouldSendAndReceiveConsumerJmsMessage() throws Exception {
        ModelCamelContext context = new DefaultCamelContext();
        ActiveMQConnectionFactory amqConnectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(5);
        
        context.addComponent("jms-test", JmsComponent.jmsComponentAutoAcknowledge(cachingConnectionFactory));
        
        context.addRoutes(new JmsRoute());
        this.camelContext = context;
        this.ct = context.createConsumerTemplate();
        this.pt = context.createProducerTemplate();
        camelContext.start();
        Customer cust = Customer.builder().login("przodownik").lastName("borowiec").firstName("slawek").salary(BigDecimal.valueOf(33.22)).build();
        pt.sendBody("seda:start", ExchangePattern.InOnly, cust);
        Customer custLoaded = ct.receiveBody("jms-test:queue.A",Customer.class);
        log.info("from queue :  {}",custLoaded);
        Thread.sleep(1000);
        camelContext.stop();
    }

    class JmsRoute extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("seda:start").log(LoggingLevel.INFO, "myCamel", "body is ${body}").to("jms-test:queue.A");
        //    from("jms-test:queue.A").log(LoggingLevel.INFO, "myCamel", "receive is ${body}").beanRef("messageBean");
            
        }
    };
    @Test
    public void shouldSendAndReceiveConsumerAsyncJmsMessage() throws Exception {
        SimpleRegistry registry = new SimpleRegistry();
        registry.put("messageBean", new MessageBean());
        ModelCamelContext context = new DefaultCamelContext(registry);
        ActiveMQConnectionFactory amqConnectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqConnectionFactory);
        context.addComponent("jms-test", JmsComponent.jmsComponentAutoAcknowledge(cachingConnectionFactory));
        
        context.addRoutes(new JmsRouteAsync());
        this.camelContext = context;
        this.ct = context.createConsumerTemplate();
        this.pt = context.createProducerTemplate();
        camelContext.start();
        Random r = new Random();
        for (int i = 0; i < 35; i++) {
        pt.sendBody("seda:start", ExchangePattern.InOnly, Customer.builder().login("przodownik "+i).lastName("borowiec").firstName("slawek").salary(BigDecimal.valueOf(r.nextGaussian())).build());
        }
        Thread.sleep(1500);
        camelContext.stop();
    }
    class JmsRouteAsync extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("seda:start").to("jms-test:queue.A");
            from("jms-test:queue.A").beanRef("messageBean").log(LoggingLevel.INFO, "myCamel", "receive is ${body}");
            
        }
    };
}
