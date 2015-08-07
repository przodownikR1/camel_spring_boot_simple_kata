package pl.java.scalatech.composed;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class ComposedTest extends CommonCreateCamelContext {
    @Test
    public void shouldComposedWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1000);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("file://inbox?noop=true").split(xpath("//person"), new MyAggregator()).to("log:myCamel?level=INFO&showAll=false&multiline=true");
        }
    }
}
