package pl.java.scalatech.soapJaxb;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class SoapJaxbTest extends CommonCreateCamelContext {
    @Test
    public void shouldSoapJaxb() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 2000);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("file://src/main/resources/?noop=true&fileName=bookStore.xml")
            // unmarshal(dataFormat)
                    .log(LoggingLevel.INFO, "myCamel", "${body}");

        }
    }
}