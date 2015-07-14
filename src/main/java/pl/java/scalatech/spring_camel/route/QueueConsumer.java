package pl.java.scalatech.spring_camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumer extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("test-activemq:queue:test").to("log:pl.java.scalatech?level=INFO");
    }

}
