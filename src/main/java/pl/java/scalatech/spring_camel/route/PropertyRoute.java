package pl.java.scalatech.spring_camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("property")
public class PropertyRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{from.start}}").to("{{to.end}}");

    }

}
