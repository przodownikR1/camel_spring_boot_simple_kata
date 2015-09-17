package pl.java.scalatech.direct;

import org.apache.camel.FailedToStartRouteException;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class MultiDirectProblemTest extends CommonCreateCamelContext{
    @Test(expected=FailedToStartRouteException.class)
    public void shouldDirectThrowException() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1200);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=200").routeId("timer").setBody(constant("slawek")).to("direct:one");
            from("direct:one").routeId("one").to("log:myCamel?level=INFO&showBody=true&multiline=true");;
            from("direct:one").routeId("two").to("log:myCamel?level=INFO&showBody=true&multiline=true");;
        }
}
}

