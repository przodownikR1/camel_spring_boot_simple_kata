package pl.java.scalatech.delayer;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class DelayTest extends CommonCreateCamelContext {
    @Test
    public void shouldSamplingWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 2000);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=100").convertBodyTo(String.class).setBody(simple("Hello przodownik")).setHeader("id", simple("YA")).delay().method(DelayBean.class, "delay")
                    .to("log:myCamel?level=INFO&showAll=false&multiline=true");
        }
    }

}
