package pl.java.scalatech.parallel;

import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spi.Synchronization;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;
@Slf4j
public class TheadRoutesTest extends CommonCreateCamelContext{
    @Test   
    public void shouldNotParallelWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1500);
        log.info("+++++++++++++++++++ normal rotue end");
    }
    
    @Test   
    public void shouldParallelWork() throws Exception {
        createContextWithGivenRoute(new MyParallelRouteBuilder(), 1500);
        log.info("+++++++++++++++++++ parallel rotue end");
    } 
    @Test
    public void shouldSendAsynsMessage() throws InterruptedException, Exception {
        ModelCamelContext context = new DefaultCamelContext();
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
              from("seda:start").transform().constant("przodownik").log(LoggingLevel.INFO,"myCamel","====  send ${body} :  ${threadName}").delay(500).transform(simple("Processed ${body}"));
            }
        });
        context.start();
        Future<?> future =pt.asyncRequestBody("seda:start", "Hello World"); 
        while(!future.isDone()) {
            log.info("Doing something else while processing...");
            Thread.sleep(200);
            }
        log.info("+++      result: {}",future.get());
        Thread.sleep(1000);
        context.stop();
    }
    @Test
    public void shouldSendAsyncAndResultAsCallback() throws Exception {
        ModelCamelContext context = new DefaultCamelContext();
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
              from("seda:start").transform().constant("przodownik").log(LoggingLevel.INFO,"myCamel","====  send ${body} :  ${threadName}").delay(500).transform(simple("Processed ${body}"));
            }
        });
        context.start();
        pt.asyncCallbackSendBody("seda:start", "test", new Synchronization() {
            @Override
            public void onComplete(Exchange exchange) {
                Assertions.assertThat(exchange.getOut().getBody()).isEqualTo("Processed przodownik");
            }
            @Override
            public void onFailure(Exchange exchange) {
                Assert.fail();
            }
        });
        Thread.sleep(1000);
        context.stop();
    }
    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=100").routeId("timer").setBody(constant("slawek")).to("direct:start");
            from("direct:start").routeId("normalRoute").log(LoggingLevel.INFO,"myCamel","++++  ${body} :  ${threadName}").delay(400).end();
            
        }
}
    class MyParallelRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=100").routeId("timer").setBody(constant("slawek")).to("direct:start");
            from("direct:start").routeId("parallelRoute").log(LoggingLevel.INFO,"myCamel","++++ start ${body} :  ${threadName}").threads().delay(400).log(LoggingLevel.INFO,"myCamel","++++ end  ${body} :  ${threadName}").end();
            
        }
}
    
   
}
