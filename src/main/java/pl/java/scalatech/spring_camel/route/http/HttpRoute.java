package pl.java.scalatech.spring_camel.route.http;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import pl.java.scalatech.spring_camel.eip_beans.RecipientListBean;
@Component
public class HttpRoute extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        RecipientListBean rl = new RecipientListBean("file://outbox?fileName=${date:now:yyyyMMddHHmmss}.xml", "stream:out");
        from("timer://nbp?fixedRate=true&delay=0&period=5000")
        .to("http://www.nbp.pl/kursy/xml/LastA.xml")
        .convertBodyTo(String.class).recipientList(method(rl, "route"));
        
        
    }

}