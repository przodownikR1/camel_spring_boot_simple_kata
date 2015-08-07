package pl.java.scalatech.resequencer;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class ResequencerTest extends CommonCreateCamelContext {
    @Test
    public void shouldSplitPersonsToPerson() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1500);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=500").convertBodyTo(String.class).setBody(simple("Hello przodownik1")).to("direct:req");
            from("timer:fire?period=500").convertBodyTo(String.class).setBody(simple("Hello przodownik3")).to("direct:req");
            from("timer:fire?period=500").convertBodyTo(String.class).setBody(simple("Hello przodownik2")).to("direct:req");
            from("timer:fire?period=500").convertBodyTo(String.class).setBody(simple("Hello przodownik4")).to("direct:req");

            from("direct:req").resequence().body().batch().size(4).to("log:myCamel?level=INFO&showAll=false&multiline=true");

        }
    }

}
