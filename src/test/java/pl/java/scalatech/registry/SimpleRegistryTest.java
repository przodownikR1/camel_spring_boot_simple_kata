package pl.java.scalatech.registry;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.junit.Test;

public class SimpleRegistryTest {
    @Test
    public void shouldRegisterWork() throws Exception {
        SimpleRegistry registry = new SimpleRegistry();
        registry.put("simpleBean", new SimpleBean());
        CamelContext context = new DefaultCamelContext(registry);

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:start").to("bean:simpleBean").to("mock:end");

            }
        });
        context.start();
        context.setTracing(true);
        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:start", "przodownik");
        context.stop();

    }
}
