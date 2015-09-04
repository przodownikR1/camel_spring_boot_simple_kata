package pl.java.scalatech.aggregate;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.assertj.core.util.Sets;

import pl.java.scalatech.spring_camel.csv.Invoice;
import pl.java.scalatech.spring_camel.csv.SummaryInvoice;

@Slf4j
public class InvoiceAggregator implements AggregationStrategy {
    private Set<SummaryInvoice> set = Sets.newHashSet();

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Invoice newInvoice = newExchange.getIn().getBody(Invoice.class);
       
        if (oldExchange == null) {
             set.add(new SummaryInvoice(newInvoice.getId(),newInvoice.getCredit(),newInvoice.getDebit(),newInvoice.getDebit()-newInvoice.getCredit()));
             newExchange.getIn().setBody(set);
            return newExchange; 
            }
       
        set = oldExchange.getIn().getBody(Set.class);
        if(set.stream().filter(t->t.getId()==newInvoice.getId()).findFirst().isPresent()) {
            SummaryInvoice si = set.stream().filter(t->t.getId()==newInvoice.getId()).findFirst().get();
            si.setCredit(si.getCredit()+newInvoice.getCredit());
            si.setDebit(si.getDebit()+newInvoice.getDebit());
            si.setSaldo(si.getSaldo()+(newInvoice.getCredit()-newInvoice.getDebit()));
        }else {
            set.add(new SummaryInvoice(newInvoice.getId(),newInvoice.getCredit(),newInvoice.getDebit(),newInvoice.getDebit()-newInvoice.getCredit()));
            
        }
      //  log.info("++++ aggregate :  {}", oldExchange.getIn().getBody(Map.class));
        return oldExchange;
    
     
        
        
        
        
    }

}
