package pl.java.scalatech.spiltter;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class FileSplitterTest extends CommonCreateCamelContext {
    @Test
    public void shouldSplitFileContent() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 2000);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("file://string?noop=true").split(body().tokenize("\n")).streaming().transform(body().convertToString()).throttle(5).timePeriodMillis(1000)
                    .log(LoggingLevel.INFO, "myCamel", "${body}");

        }

    }

}
