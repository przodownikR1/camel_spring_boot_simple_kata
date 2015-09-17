package pl.java.scalatech.file;

import lombok.extern.slf4j.Slf4j;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;


import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class CreateFileTest extends CommonCreateCamelContext {
    @Test
    public void shouldCreateFile() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1200);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("timer:fire?period=200").setBody(constant("slawek")).process(exchange -> {
                log.info("+++ {}",exchange.getIn().getHeaders());
                log.info("+++ properties {}",exchange.getIn().getExchange().getProperties());
                exchange.getIn().getHeaders().put("counter", exchange.getIn().getExchange().getProperty("CamelTimerCounter"));
                exchange.getIn().setBody(exchange.getIn().getHeader("firedTime").toString());
            }).log("++++ ${headers}").to("direct:start");
            
            from("direct:start").setHeader(Exchange.FILE_NAME, simple("${header.counter}.done")).to("file://inbox").end();
        }
    }
}
