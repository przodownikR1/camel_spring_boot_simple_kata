package pl.java.scalatech.resequencer;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentAutoAcknowledge;

import javax.jms.ConnectionFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ReseqTest extends CamelTestSupport {

    @Test
    public void testBatchResequencerJMSPriority() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("G", "A", "B", "E", "H", "C", "D", "F");

        // must use preserveMessageQos=true to be able to specify the JMSPriority to be used
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "A", "JMSPriority", 6);
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "B", "JMSPriority", 6);
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "C", "JMSPriority", 4);
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "D", "JMSPriority", 4);
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "E", "JMSPriority", 6);
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "F", "JMSPriority", 4);
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "G", "JMSPriority", 8);
        template.sendBodyAndHeader("jms:queue:foo?preserveMessageQos=true", "H", "JMSPriority", 6);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();

        ConnectionFactory connectionFactory = CamelJmsTestHelper.createConnectionFactory();
        camelContext.addComponent("jms", jmsComponentAutoAcknowledge(connectionFactory));

        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // START SNIPPET: e1
                from("jms:queue:foo")
                // sort by JMSPriority by allowing duplicates (message can have same JMSPriority)
                // and use reverse ordering so 9 is first output (most important), and 0 is last
                // use batch mode and fire every 3th second
                        .resequence(header("JMSPriority")).batch().timeout(3000).allowDuplicates().reverse().to("mock:result");
                // END SNIPPET: e1
            }
        };
    }
}
