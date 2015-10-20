package pl.java.scalatech.spiltter.clazz;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SimpleWrapperSplitterTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            
            @Override  
            public void configure() throws Exception {  //.parallelProcessing() over to a thread pool
              //.timeout(5000)  
                from("direct:start").bean(Wrapper.class, "createList").split().body().stopOnException().
                choice().when().simple("${property.CamelSplitComplete}").log(LoggingLevel.INFO,"myCamel"," END  ${body} index : ${property.CamelSplitIndex}")
                .otherwise().
                log(LoggingLevel.INFO,"myCamel","${body} ${property.CamelSplitIndex}  ${property.CamelSplitSize}")       
                 .end();
                
            }
        };
    }
    
    @Test
    public void shouldSplitWork() throws Exception {
      
        template.sendBody("direct:start", null);
       
        Thread.sleep(2000);
        context.stop();  
    }
}
