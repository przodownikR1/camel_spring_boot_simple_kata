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
        log.info("new headers  {}",newExchange.getIn().getHeaders());
        if (oldExchange == null) {
             map.put(newInvoice.getId(), new SummaryInvoice(newInvoice.getCredit(),newInvoice.getDebit(),newInvoice.getDebit()-newInvoice.getCredit()));
             newExchange.getIn().setBody(map);
            return newExchange; 
            }
        log.info("old headers  {}",oldExchange.getIn().getHeaders());
        map = oldExchange.getIn().getBody(Map.class);
        if(map.containsKey(newInvoice.getId())) {
            SummaryInvoice si = map.get(newInvoice.getId());
            si.setCredit(si.getCredit()+newInvoice.getCredit());
            si.setDebit(si.getDebit()+newInvoice.getDebit());
            si.setSaldo(si.getSaldo()+(newInvoice.getCredit()-newInvoice.getDebit()));
        }else {
            map.put(newInvoice.getId(), new SummaryInvoice(newInvoice.getCredit(),newInvoice.getDebit(),newInvoice.getDebit()-newInvoice.getCredit()));
            
        }
        log.info("++++ aggregate :  {}", oldExchange.getIn().getBody(Map.class));
        return oldExchange;
    
     
        
        
        
        
    }

}
