package pl.java.scalatech.nbp;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Test;

@Slf4j
public class ReadFileXmlGuavaTest {
    @Test
    public void shouldReadAndParseXmlFileWork() throws Exception {
        SimpleRegistry registry = new SimpleRegistry();
        registry.put("fileContentReader", new FileContentLoader());
        ModelCamelContext context = new DefaultCamelContext(registry);
        context.addRoutes(new MyRouteXmlParseBuilder());
        context.start();
        context.setTracing(true);
        Thread.sleep(1000);

        context.getRouteDefinitions().forEach(route -> {
            log.info(route.toString());
            route.getInputs().forEach(in -> log.info("Input: " + in.getUri()));
            route.getOutputs().forEach(out -> log.info("Out: " + out.getClass()));
        });
        context.stop();
    }

    class MyRouteXmlParseBuilder extends RouteBuilder {
        @Override
        public void configure() {
            onException(java.io.FileNotFoundException.class).handled(true);
            from("file:incoming/test?noop=true").to("direct:reader");
            from("direct:reader").beanRef("fileContentReader").setId("READ_CONTENT_FILE_ROUTE");

        }
    }
}
