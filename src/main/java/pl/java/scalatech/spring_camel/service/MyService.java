package pl.java.scalatech.spring_camel.service;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyService {
    @EndpointInject
    ProducerTemplate producer;

    public void invokeRoutes() {
        producer.sendBody("seda:start", "Hello world");
    }

}