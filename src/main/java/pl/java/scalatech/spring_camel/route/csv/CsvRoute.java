package pl.java.scalatech.spring_camel.route.csv;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pl.java.scalatech.spring_camel.processor.FileNameProcessor;

@Component
@Profile("csv")
public class CsvRoute extends RouteBuilder {

    @Autowired
    private FileNameProcessor fnp;

    BindyCsvDataFormat bindy = new BindyCsvDataFormat("pl.java.scalatech.spring_camel.beans");

    @Override
    public void configure() throws Exception {
        from("direct:csv").marshal(bindy).process(fnp).to("file://outbox");//?fileName=${header.count}.txt
        //.setHeader("CamelFileName", constant("report.txt")).
    }

}
