package pl.java.scalatech.splitFileAggragate;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.ModelCamelContext;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;
import pl.java.scalatech.aggregate.InvoiceAggregator;

public class SplitAndAggregateInvoiceTest extends CommonCreateCamelContext {
    @Test
    public void shouldAggregateWork() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 3000);
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
            from("file://inbox?fileName=invoice.csv&noop=true").routeId("Bindy :: Invoice bindy  ").unmarshal(bindy).split(body())

            .setHeader("id", simple("${body.id}")).aggregate(header("id"), new InvoiceAggregator()).completionSize(4)
            /* .completionPredicate(simple("${body.size}==4")) */
            .log(LoggingLevel.INFO, "myCamel", "OOOO                  ${body}  Completed by ${property.CamelAggregatedCompletedBy} ");
        }
    }
}
