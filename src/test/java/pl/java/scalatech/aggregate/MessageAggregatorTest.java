package pl.java.scalatech.aggregate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SetHeaderDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.aggregate.generator.MessageGenerator;
@Slf4j
public class MessageAggregatorTest extends CamelTestSupport{

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        System.out.println("testing");
        return new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {
                from("direct:start").bean(MessageGenerator.class)
                .bean(new MessageAgg())
                .split().body().
                 setHeader("aggId").simple("${body.meta.aggId}")
                .setHeader("aggIdTotal").simple("${body.meta.total}")
               
                .aggregate(header("aggId"),new MessageAgg()).completionSize(simple("${header.aggIdTotal}"))
                //
                .bean(new MessageAgg());                
                
            }
        };
    }
    
    @Test
    public void shouldAggregatorCompletionSizeWork() throws Exception {
        template.sendBody("direct:start", 8);
        Thread.sleep(5000);
        context.stop();  
    }
    
}
