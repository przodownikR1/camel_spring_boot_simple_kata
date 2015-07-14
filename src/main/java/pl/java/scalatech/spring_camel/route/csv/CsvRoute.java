package pl.java.scalatech.spring_camel.route.csv;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.stereotype.Component;

@Component
public class CsvRoute extends RouteBuilder {
    BindyCsvDataFormat bindy = new BindyCsvDataFormat("pl.java.scalatech.spring_camel.beans");

    @Override
    public void configure() throws Exception {
        from("direct:csv").marshal(bindy).to("file://outbox");
    }

}
