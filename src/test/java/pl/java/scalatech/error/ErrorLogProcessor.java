package pl.java.scalatech.error;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
@Slf4j
public class ErrorLogProcessor implements Processor{
        @Override
        public void process(Exchange exchange) throws Exception {
         
            Throwable e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
            log.info("+++  error :  {}",exchange.getProperty("CamelExceptionCaught"));
            exchange.getProperties().entrySet().stream().forEach(t->log.info("{} => {}",t.getKey(),t.getValue()));
            log.info("+++  body :  {}", exchange.getIn().getBody());
            log.info("+++  headers  :  {}", exchange.getIn().getHeaders());
            log.info("+++  exception Processor : {}", e);
        }
    };

