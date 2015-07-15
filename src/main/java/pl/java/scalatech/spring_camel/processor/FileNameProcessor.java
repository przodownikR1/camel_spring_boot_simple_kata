package pl.java.scalatech.spring_camel.processor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileNameProcessor implements Processor {
    @Value("${filePattern}")
    private String filePattern;

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(Exchange.FILE_NAME, generateFileName());
    }

    private String generateFileName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(filePattern)).trim();
    }
}