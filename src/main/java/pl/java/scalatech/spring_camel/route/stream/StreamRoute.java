package pl.java.scalatech.spring_camel.route.stream;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class StreamRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            log.info("+++  stream start");
            from("stream:in?promptMessage=Enter something: ").transform().simple("${body.toUpperCase()}").to("stream:out");

        }
}
