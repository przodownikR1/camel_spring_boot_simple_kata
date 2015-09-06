package pl.java.scalatech.error;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@Slf4j
public class OnExceptionHandlerTest {
    private static AtomicInteger count = new AtomicInteger();

    @Test(expected=CamelExecutionException.class)
    public void shouldOnFalseException() throws Exception {
        log.info("++++++++++++++++++++++++++++++ false +++++++++++++++++++++++++++++++++++++");
        ModelCamelContext context = new DefaultCamelContext();
        context.setTracing(true);
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(IllegalStateException.class).handled(false).to("seda:error");
                from("seda:start").log(LoggingLevel.INFO, "myCamel", "+++ ${body}").setProperty("h1").constant("przodownik").bean(SampleErrorBean.class)
                        .bean(ErrorBean.class).bean(SampleErrorBean.class).transform(constant("ok"));
                from("seda:error").process(new ErrorLogProcessor());
            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("test");
        Thread.sleep(2000);
    }

    @Test
    public void shouldOnTrueException() throws Exception {
        log.info("++++++++++++++++++++++++++++++ true +++++++++++++++++++++++++++++++++++++");
        ModelCamelContext context = new DefaultCamelContext();
        context.setTracing(true);
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(IllegalStateException.class).handled(true).to("seda:error");
                from("seda:start").log(LoggingLevel.INFO, "myCamel", "+++ ${body}").setProperty("h1").constant("przodownik").bean(SampleErrorBean.class)
                        .bean(ErrorBean.class).bean(SampleErrorBean.class).transform(constant("ok"));
                from("seda:error").process(new ErrorLogProcessor());
            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("test");
        Thread.sleep(2000);
    }
    
  
    
    @Test
    public void shouldOnContinueException() throws Exception {
        log.info("++++++++++++++++++++++++++++++  continue+++++++++++++++++++++++++++++++++++++");
        ModelCamelContext context = new DefaultCamelContext();
        context.setTracing(true);
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                onException(IllegalStateException.class).continued(true);
                from("seda:start").log(LoggingLevel.INFO, "myCamel", "+++ ${body}").setProperty("h1").constant("przodownik")
                        .bean(ErrorBean.class).bean(SampleErrorBean.class).transform(constant("ok"));
            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("ok");
        Thread.sleep(2000);
    }

}
