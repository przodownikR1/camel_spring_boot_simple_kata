package pl.java.scalatech.seda;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class MultiSedaTest extends CommonCreateCamelContext{
    @Test
    public void shouldSedaConsumerWork() throws Exception {
        createContextWithGivenRoute(new MyRouteSedaConcurrentBuilder(), 1200);
    }
    
    @Test
    public void shouldSedaWork() throws Exception {
        createContextWithGivenRoute(new MyRouteSedaBuilder(), 1200);
    }
    
    @Test
    public void shouldDirectWork() throws Exception {
        createContextWithGivenRoute(new MyRouteDirecteBuilder(), 2200);
    }

    class MyRouteSedaConcurrentBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=100").routeId("timer").setBody(constant("slawek")).to("seda:one");
            from("seda:one?concurrentConsumers=10").routeId("one").delay(1000).log(LoggingLevel.INFO,"myCamel","++++ seda  concurrent consumer ${body} :  ${threadName}");
           // from("seda:one").routeId("two").to("log:myCamel?level=INFO&showBody=true&multiline=true");
        }
}
    
    class MyRouteSedaBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=100").routeId("timer").setBody(constant("slawek")).to("seda:one");
            from("seda:one").routeId("one").delay(1000).log(LoggingLevel.INFO,"myCamel","++++ seda ${body} : ${threadName}");
           // from("seda:one").routeId("two").to("log:myCamel?level=INFO&showBody=true&multiline=true");
        }
}
    class MyRouteDirecteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=200").routeId("timer").setBody(constant("slawek")).to("direct:one");
           from("direct:one").routeId("oneDirect").delay(1000).log(LoggingLevel.INFO,"myCamel","++++ direct  ${body} : ${threadName}");
        }
        }
}
