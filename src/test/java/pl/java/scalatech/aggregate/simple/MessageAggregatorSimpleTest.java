package pl.java.scalatech.aggregate.simple;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageAggregatorSimpleTest extends CamelTestSupport{

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        System.out.println("testing");
        return new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {
                from("direct:start")
                .aggregate(header("id"),new SimpleAgg()).completionSize(header("total")).
                //
                //.log(LoggingLevel.INFO,"myCamel","${body}");
               bean(new SimpleAgg());                
                
            }
        };
    }
    
    @Test
    public void shouldAggregatorSimple() throws Exception {
        Map<String,Object> headers = newHashMap();
        headers.put("id",1);
        headers.put("total",3);
        template.sendBodyAndHeaders("direct:start", "A",headers);
        
        headers = newHashMap();
        headers.put("id",2);
        headers.put("total",2);
        template.sendBodyAndHeaders("direct:start", "B",headers);
        
        headers = newHashMap();
        headers.put("id",3);
        headers.put("total",1);
        template.sendBodyAndHeaders("direct:start", "C",headers);
        
        headers = newHashMap();
        headers.put("id",1);
        headers.put("total",3);
        template.sendBodyAndHeaders("direct:start", "D",headers);
        
        headers = newHashMap();
        headers.put("id",2);
        headers.put("total",2);
        template.sendBodyAndHeaders("direct:start", "E",headers);
        
        headers = newHashMap();
        headers.put("id",1);
        headers.put("total",3);
        template.sendBodyAndHeaders("direct:start", "F",headers);
        
        Thread.sleep(2000);
        context.stop();  
    }
    
}
