package pl.java.scalatech.log_timer;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.processor.interceptor.Tracer;
import org.junit.Test;

import pl.java.scalatech.nbp.FileContentLoader;

@Slf4j
public class TimerAndLogTest {
    @Test
    public void shouldReadAndParseXmlFileWork() throws Exception {
        SimpleRegistry registry = new SimpleRegistry();
        registry.put("fileContentReader", new FileContentLoader());
        ModelCamelContext context = new DefaultCamelContext(registry);

        Tracer tracer = new Tracer();
        tracer.setLogName("MyTracerLog");
        tracer.getDefaultTraceFormatter().setShowProperties(true);
        tracer.getDefaultTraceFormatter().setShowHeaders(false);
        tracer.getDefaultTraceFormatter().setShowBody(true);

        context.addInterceptStrategy(tracer);
        context.addRoutes(new MyRouteBuilder());
        context.setTracing(true);
        context.start();

        Thread.sleep(1200);
        context.getRouteDefinitions().forEach(route -> {
            log.info(route.toString());
            route.getInputs().forEach(in -> log.info("Input: " + in.getUri()));
            route.getOutputs().forEach(out -> log.info("Out: " + out.getClass()));
        });
        context.stop();

    }

    class MyRouteBuilder extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("quartz2://myGroup/przodownik?cron=*+*+10-18+?+*+MON-FRI").transform().simple("####MESSAGE### -> ${header.firedTime}")
                    .process(exchange -> log.info("headers {}", exchange.getIn().getHeaders()))
                    .log(LoggingLevel.INFO, "myCamel", " next : ${header.nextFireTime} , jobDetail : ${header.jobDetail}")
                    .to("log:myCamel?level=INFO&showAll=true&multiline=true");
        }
    }
}
