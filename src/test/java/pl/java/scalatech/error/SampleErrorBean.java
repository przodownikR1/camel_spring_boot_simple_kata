package pl.java.scalatech.error;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Body;
import org.apache.camel.Exchange;

@Slf4j
public class SampleErrorBean {
    public String log(@Body String str,Exchange exchange,Throwable th) {
           exchange.getProperties().entrySet().stream().forEach(t->log.info("{} => {}",t.getKey(),t.getValue()));
           Object o  = exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
           Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
           log.info("##### body :  {}", exchange.getIn().getBody());
          // log.info("+++ properties  {}",exchange.getProperties());
          
           log.info("##### headers  {}",exchange.getIn().getHeaders());
           log.info("##### th : {}",cause);
           return str;
    }
}