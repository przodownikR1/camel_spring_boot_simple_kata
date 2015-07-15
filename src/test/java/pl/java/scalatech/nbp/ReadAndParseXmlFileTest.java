package pl.java.scalatech.nbp;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Test;

@Slf4j
public class ReadAndParseXmlFileTest {
    @Test
    public void shouldReadAndParseXmlFileWork() throws Exception {
        SimpleRegistry registry = new SimpleRegistry();
        registry.put("fileContentReader", new FileContentLoader());
        ModelCamelContext context = new DefaultCamelContext(registry);
        context.addRoutes(new MyRouteXmlParseBuilder());
        context.start();
        context.setTracing(true);
        Thread.sleep(2000);
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
            from("file:incoming?noop=true").choice().when(header(Exchange.FILE_NAME).endsWith(".xml")).process(exchange -> {
                log.info("+++ xml processing");

                //  log.info("xml in : {}",exchange.getIn().getBody());
                    log.info("xml header {}", exchange.getIn().getHeaders());
                    // log.info("xml out : {}",exchange.getOut());

                }).to("file:outbox/xml").when(header(Exchange.FILE_NAME).regex("^.*(csv|csl)$")).process(exchange -> {
                log.info("+++ csv processing");
            }).to("jms:outbox/csv").otherwise().process(exchange -> {
                log.info("+++ other processing");
            }).to("file:outbox/other").setId("Content_ROUTE");

            from("file:xml?noop=true").process(exchange -> {
                log.info("in : {}", exchange.getIn().getBody());
                log.info("header {}", exchange.getIn().getHeaders());
                log.info("out : {}", exchange.getOut());

            }).split(body().tokenize("pozycja")).streaming().to("log:pl.java.scalatech?level=INFO").setId("ProcessXML_ROUTE");

        }
    }

}