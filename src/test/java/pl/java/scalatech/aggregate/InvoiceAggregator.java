package pl.java.scalatech.aggregate;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.assertj.core.util.Maps;

import pl.java.scalatech.spring_camel.csv.Invoice;
import pl.java.scalatech.spring_camel.csv.SummaryInvoice;

@Slf4j
public class InvoiceAggregator implements AggregationStrategy {
    private Map<Long, SummaryInvoice> map = Maps.newHashMap();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Invoice newInvoice = newExchange.getIn().getBody(Invoice.class);

        if (oldExchange == null) { return newExchange; }
        if (oldExchange != null) {

            Invoice invoice = oldExchange.getIn().getBody(Invoice.class);
            invoice.setCredit(invoice.getCredit() + newInvoice.getCredit());
            invoice.setDebit(invoice.getDebit() + newInvoice.getDebit());
            oldExchange.getIn().setBody(invoice);

        }
        log.info("++++ aggregate :  {}", oldExchange.getIn().getBody(Invoice.class));
        return oldExchange;
    }

}
