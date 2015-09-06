package pl.java.scalatech.error;

import javax.jms.ConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import static org.apache.camel.component.jms.JmsComponent.jmsComponentAutoAcknowledge;
public class JmsDeadLetterQueueTest extends CamelTestSupport {

    protected String getUri() {
        return "activemq:queue:dead";
    }

    @Test
    public void testOk() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Hello World");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testError() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:dead");
        mock.expectedBodiesReceived("test");

        template.sendBody("direct:start", "test");

        assertMockEndpointsSatisfied();

        // the cause exception is gone in the transformation below
        assertNull(mock.getReceivedExchanges().get(0).getProperty(Exchange.EXCEPTION_CAUGHT));
    }

    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();

        ConnectionFactory connectionFactory = CamelJmsTestHelper.createConnectionFactory();
        camelContext.addComponent("activemq", jmsComponentAutoAcknowledge(connectionFactory));

        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                errorHandler(deadLetterChannel("seda:dead").disableRedelivery());

                from("direct:start").process(exchange ->
                {
                    String body = exchange.getIn().getBody(String.class);
                    if ("test".equals(body)) {
                        throw new IllegalArgumentException("test");
                    }
                }).to("mock:result");

                from("seda:dead").transform(exceptionMessage()).to(getUri());

                from(getUri()).to("mock:dead");
            }
        };
    }

}