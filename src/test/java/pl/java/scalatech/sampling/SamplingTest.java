package pl.java.scalatech.sampling;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class SamplingTest extends CommonCreateCamelContext {
    @Test
    public void shoulDelayWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1300);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=100").convertBodyTo(String.class).setBody(simple("Hello przodownik")).setHeader("id", simple("YA")).sample(3)
                    .to("log:myCamel?level=INFO&showAll=false&multiline=true");
        }
    }

}
