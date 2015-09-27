package pl.java.scalatech.timer;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class SampleTimerSplitTest extends CommonCreateCamelContext {

    @Test
    public void shouldAggregateWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1000);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer://timer1?period=200&daemon=false").routeId("splitTimer").setBody(constant("slawek")).setHeader("ids", body())
                    .log(LoggingLevel.INFO,"myCamel","+++ ${body}").to("mock:splitTimer");

        }
    }

}
