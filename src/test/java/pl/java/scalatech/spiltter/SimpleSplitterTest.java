package pl.java.scalatech.spiltter;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class SimpleSplitterTest extends CommonCreateCamelContext {
    @Test
    public void shouldSplitPersonsToPerson() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1200);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("file://inbox?noop=true").split().xpath("//person").to("log:myCamel?level=INFO&showAll=false&multiline=true");
        }
    }
}