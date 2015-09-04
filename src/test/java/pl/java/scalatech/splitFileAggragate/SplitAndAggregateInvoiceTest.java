package pl.java.scalatech.splitFileAggragate;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.ModelCamelContext;
import org.assertj.core.util.Lists;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;
import pl.java.scalatech.aggregate.InvoiceAggregator;

public class SplitAndAggregateInvoiceTest extends CommonCreateCamelContext {
    @Test
    public void shouldAggregateWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(),4000);
    }

    class MyRouteBuilder extends RouteBuilder {
        BindyCsvDataFormat bindy;

        @Override
        public ModelCamelContext getContext() {
            bindy = new BindyCsvDataFormat(pl.java.scalatech.spring_camel.csv.Invoice.class);

            return super.getContext();
        }

        @Override
        public void configure() throws Exception {
            from("file://inbox?fileName=invoice.csv&noop=true").routeId("Bindy :: Invoice bindy  ").removeHeaders("CamelFile*").unmarshal(bindy).split(body())

            //.setHeader("id", simple("${body.id}")).aggregate(header("id"), new InvoiceAggregator()).completionTimeout(1500)
            .setHeader("id", simple("${body.id}")).aggregate(constant(true), new InvoiceAggregator()).completionSize(8).parallelProcessing()
            //.setHeader("id", simple("${body.id}")).aggregate(header("id"), new GroupedExchangeAggregationStrategy()).completionTimeout(1500)
          
             .split().body()
            .log(LoggingLevel.INFO, "myCamel", "OOOO      ${threadName}        ${body}  Completed by ${property.CamelAggregatedCompletedBy}  size:   ");
                   
        }
    }
}
 class BatchSizePredicate implements Predicate {

    public int size;

    public BatchSizePredicate(int size) {
            this.size = size;
    }

    @Override
    public boolean matches(Exchange exchange) {
            if (exchange != null) {
                    ArrayList list = exchange.getIn().getBody(ArrayList.class);
                    if (!Lists.emptyList().isEmpty() && list.size() == size) {
                            return true;
                    }
            }
            return false;
    }

}