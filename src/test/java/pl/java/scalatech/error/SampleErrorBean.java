package pl.java.scalatech.error;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Body;
import org.apache.camel.Exchange;

@Slf4j
public class SampleErrorBean {
    public String log(@Body String str,Exchange exchange,Throwable th) {
           exchange.getProperties().entrySet().stream().forEach(t->log.info("{} => {}",t.getKey(),t.getValue()));
           log.info("+++  body :  {}", exchange.getIn().getBody());
           log.info("+++ th : {}",th);
           return str;
    }
}