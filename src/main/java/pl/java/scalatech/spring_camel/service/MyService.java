package pl.java.scalatech.spring_camel.service;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyService {
    @EndpointInject
    ProducerTemplate producer;

    public void invokeRoutes() {
        producer.sendBody("seda:start", "Hello world");
    }

   // @Scheduled(fixedDelay = 5000)
    public void invokeExampleRoute() {
        producer.sendBody("direct:exampleRoute", "Good morning");
    }

   // @Scheduled(fixedDelay = 5000)
    public void invokeQueueRoute() {
        producer.sendBody("direct:qStart", "Good morning");
    }

}