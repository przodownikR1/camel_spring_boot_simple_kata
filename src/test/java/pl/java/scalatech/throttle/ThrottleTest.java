package pl.java.scalatech.throttle;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class ThrottleTest extends CommonCreateCamelContext {
    @Test
    public void shouldThrottleWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1100);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=100").convertBodyTo(String.class).setBody(simple("Hello przodownik")).setHeader("id", simple("YA")).throttle(2)
                    .timePeriodMillis(1000).to("log:myCamel?level=INFO&showAll=false&multiline=true");
        }
    }

}
