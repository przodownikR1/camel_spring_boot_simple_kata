package pl.java.scalatech.error;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.processor.DefaultErrorHandler;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@Slf4j
public class DefaultErrorTest {
    private static AtomicInteger count = new AtomicInteger();
   @Test(expected=CamelExecutionException.class)
    public void shouldDefaultErrorHandler() throws Exception {
        ModelCamelContext context = new DefaultCamelContext();
        context.setTracing(true);
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("seda:start").errorHandler(defaultErrorHandler())
                        .log(LoggingLevel.INFO, "myCamel", "+++ ${body}").bean(SampleErrorBean.class).process(new ErrorLogProcessor()).bean(ErrorBean.class)
                        .bean(SampleErrorBean.class).transform(constant("ok"));

            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("ok");
        Thread.sleep(5000);
    }

  
}
