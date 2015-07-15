package pl.java.scalatech.log_timer;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

@Slf4j
public class TimerAndLogTest extends CommonCreateCamelContext {
    @Test
    public void shouldReadAndParseXmlFileWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1200);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("quartz2://myGroup/przodownik?cron=*+*+10-18+?+*+MON-FRI").transform().simple("####MESSAGE### -> ${header.firedTime}").setHeader("type")
                    .constant("customer").process(exchange -> log.info("headers {}", exchange.getIn().getHeaders()))
                    .log(LoggingLevel.INFO, "myCamel", " next : ${header.nextFireTime}   , type : ${header.type}")
                    .to("log:myCamel?level=INFO&showAll=true&multiline=true");
        }
    }
}
