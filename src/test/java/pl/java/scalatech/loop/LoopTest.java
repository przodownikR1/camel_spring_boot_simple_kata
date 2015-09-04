package pl.java.scalatech.loop;

import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class LoopTest extends CommonCreateCamelContext {
    @Test
    public void shouldLoopWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 2200);
    }

    @Test
    public void shouldSedaWork() throws Exception {
        ModelCamelContext context = new DefaultCamelContext();
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:start").to("seda:next").transform(constant("OK"));
                from("seda:next").to("mock:result");
            }
        });
        context.start();
        String result = (String) pt.requestBody("direct:start", "test");
        Assertions.assertThat(result).isEqualTo("OK");
        Thread.sleep(1000);
        context.stop();
    }
    
    @Test
    public void shouldDirectWork() throws Exception {
        ModelCamelContext context = new DefaultCamelContext();
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:start").to("direct:next").transform(constant("OK"));
                from("direct:next").to("mock:result");
            }
        });

        context.start();
        String result = (String) pt.requestBody("direct:start", "test");
        Assertions.assertThat(result).isEqualTo("OK");
        Thread.sleep(1000);
        context.stop();
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            ProducerTemplate pt = getContext().createProducerTemplate();
            from("seda:a").autoStartup(true).transform().constant("przodownik").loop(8).log(LoggingLevel.INFO, "myCamel", "++++  ${body} :  ${threadName}");
            Future<?> result = pt.asyncRequestBody("seda:a", "test");
            log.info("+++   {}", result.get());
        }
    }

}
