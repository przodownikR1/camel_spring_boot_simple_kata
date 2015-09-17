package pl.java.scalatech.error;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.ModelCamelContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;
@Slf4j
public class DlQTest {

    private static AtomicInteger counterA = new AtomicInteger();
    private static AtomicInteger counterB = new AtomicInteger();
   
    
    @Test
    public void shouldDLQOrginalHandler() throws Exception {
      
        ModelCamelContext context = new DefaultCamelContext();
        context.setTracing(true);
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // will use original
                ErrorHandlerBuilder a = deadLetterChannel("seda:dead").
                        maximumRedeliveries(1).redeliveryDelay(300).logStackTrace(false)
                        .useOriginalMessage().logHandled(false);
                 
                from("seda:start").errorHandler(a).log(LoggingLevel.INFO,"myCamel","==== ${body}").bean(SampleErrorBean.class).bean(ChangeBody.class).bean(ErrorBean.class).bean(SampleErrorBean.class).transform(constant("ok"));
                from("seda:dead").bean(FailureBean.class).process(exchange ->
                {
                    counterA.incrementAndGet();
                    Throwable e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Throwable.class);
                    log.info("+++  properties :  {}",exchange.getProperties() );
                    log.info("+++  body :  {}",exchange.getIn().getBody() );
                    log.info("+++  headers  :  {}",exchange.getIn().getHeaders() );
                    log.info("+++  exception Processor : {}",e);
                    log.info("+++ counterA :  {} , header : {}",counterA.get(),exchange.getProperty("CamelExceptionCaught"));
                    
                }).convertBodyTo(String.class).bean(SampleErrorBean.class);
            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("test");
        Thread.sleep(3000);    
    }
    
    @Test
    public void shouldDLQHandler() throws Exception {
  
        ModelCamelContext context = new DefaultCamelContext();
        
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
              ErrorHandlerBuilder b = deadLetterChannel("direct:dead").maximumRedeliveries(3).redeliveryDelay(300).logStackTrace(true).logHandled(true);
                from("seda:start").errorHandler(b).log(LoggingLevel.INFO,"myCamel","==== ${body}").bean(SampleErrorBean.class).bean(ChangeBody.class).bean(ErrorBean.class).bean(SampleErrorBean.class).transform(constant("ok"));
                from("direct:dead").bean(FailureBean.class).process(new ErrorLogProcessor()).convertBodyTo(String.class).bean(SampleErrorBean.class);
            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("change  :: test");
        Thread.sleep(3000);    
    }

    
}
