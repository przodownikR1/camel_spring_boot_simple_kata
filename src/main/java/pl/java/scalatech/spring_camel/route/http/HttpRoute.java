package pl.java.scalatech.spring_camel.route.http;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
@Component
public class HttpRoute extends RouteBuilder {
   

    @Override
    public void configure() throws Exception {
        from("timer://nbp?fixedRate=true&delay=0&period=5000")
        .to("http://www.nbp.pl/kursy/xml/LastA.xml")
        .convertBodyTo(String.class)
        .to("stream:out");
        
    }

}