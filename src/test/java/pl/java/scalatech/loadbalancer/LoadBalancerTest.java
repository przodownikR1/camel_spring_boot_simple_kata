package pl.java.scalatech.loadbalancer;

import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class LoadBalancerTest extends CommonCreateCamelContext {
    @Test
    public void shouldLoadBalancerWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 1000);
    }

    class MyRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            log.info("+++");
            from("timer:fire?period=500").convertBodyTo(String.class).setBody(simple("Hello przodownik")).
                    loadBalance().roundRobin().to("direct:one","direct:two","direct:third");
                    from("direct:one").to("log:myCamel?level=INFO&showAll=false&multiline=true");
                    from("direct:two").to("log:myCamel?level=INFO&showAll=false&multiline=true");
                    from("direct:third").to("log:myCamel?level=INFO&showAll=false&multiline=true");
                    
        }
    }

}
