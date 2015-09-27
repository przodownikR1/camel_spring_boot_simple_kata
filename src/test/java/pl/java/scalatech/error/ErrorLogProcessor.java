package pl.java.scalatech.error;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
@Slf4j
public class ErrorLogProcessor implements Processor{
        @Override
        public void process(Exchange exchange) throws Exception {
         
            Throwable e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
            log.error("+++ERROR LOG :   error :  {}",exchange.getProperty("CamelExceptionCaught"));
            exchange.getProperties().entrySet().stream().forEach(t->log.info("property   : {}    =>       {}",t.getKey(),t.getValue()));
            log.error("+++ERROR LOG :   body :  {}", exchange.getIn().getBody());
            exchange.getIn().getHeaders().entrySet().stream().forEach(t->log.info("header   : {}    =>       {}",t.getKey(),t.getValue()));
            log.error("+++ERROR LOG :   exception Processor : {}", e);
            
        }
    };

