package pl.java.scalatech.spring_camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;

import pl.java.scalatech.spring_camel.ws.PingService;

// @Component
@Profile("cxf")
public class PingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("cxf:/PingPong?serviceClass=" + PingService.class.getName()).to("log:" + getClass().getName()).convertBodyTo(String.class).process(e -> {
            String pingRequest = e.getIn().getBody(String.class);
            e.getIn().setBody(new Object[] { "Pong: " + pingRequest });
        });

    }
}