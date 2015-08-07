package pl.java.scalatech.slip;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class SlipRouteTest extends CommonCreateCamelContext {
    @Test
    public void shouldSlip() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1000);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=500").convertBodyTo(String.class).setBody(simple("Hello przodownik1"))
                    .setHeader("slip", method(RoutingSlipBean.class, "nextSteps")).routingSlip("slip", ",");

            from("direct:one").to("log:myCamel?level=INFO&showAll=false&multiline=true");
            from("direct:two").to("log:myCamel?level=INFO&showAll=false&multiline=true");

        }

    }
}
