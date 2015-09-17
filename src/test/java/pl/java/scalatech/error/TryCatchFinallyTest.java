package pl.java.scalatech.error;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;
@Slf4j
public class TryCatchFinallyTest {
    @Test
    public void shouldTryCatchFinally() throws Exception {
      
        ModelCamelContext context = new DefaultCamelContext();
        context.setTracing(true);
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
               
                from("seda:start").log(LoggingLevel.INFO, "myCamel", "+++ ${body}").setProperty("h1").constant("przodownik").bean(SampleErrorBean.class)
                        .doTry().bean(ErrorBean.class).doCatch(IllegalStateException.class).to("seda:illegal").doFinally().bean(SampleErrorBean.class).transform(constant("ok"));
                from("seda:error").setBody().constant("error-channel").process(new ErrorLogProcessor());
                from("seda:illegal").setBody().constant("illegal-channel").process(new ErrorLogProcessor());
                
            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("ok");
        Thread.sleep(2000);
    }
}
