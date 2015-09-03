package pl.java.scalatech.file;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class FileXmlReader extends CommonCreateCamelContext {
    @Test
    public void shouldReadAndXmlFile() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1200);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("file://inbox?noop=true&fileName=person.xml").convertBodyTo(String.class).process(exchange -> {
                log.info("{}", exchange.getIn().getBody());

            }).to("log:myCamel?level=INFO&showBody=true&multiline=true");

        }
    }
}
