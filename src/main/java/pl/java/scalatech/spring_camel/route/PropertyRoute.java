package pl.java.scalatech.spring_camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PropertyRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{from.start}}").to("{{to.end}}");

    }

}
