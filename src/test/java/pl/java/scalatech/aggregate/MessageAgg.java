package pl.java.scalatech.aggregate;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.aggregate.domain.Item;

@Slf4j
public class MessageAgg implements AggregationStrategy{
    
    
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if(oldExchange != null){
        log.info("OLD :  {}",oldExchange.getIn().getBody());
        }
        log.info("NEW  :  {}",newExchange.getIn().getBody());
        Multiset<String> result = HashMultiset.create();
        Item item = newExchange.getIn().getBody(Item.class);
        saveIntoDb(item);
        if (oldExchange == null) { 
        result.add(item.getReqId());    
        newExchange.getIn().setBody(result);
        return newExchange;
        
        }
        result = oldExchange.getIn().getBody(Multiset.class);
        result.add(item.getReqId());
       
        
       return oldExchange;
    }
    
    private void saveIntoDb(Item item){
        log.info(" item : {} = > agg:   {} : nr : {} , total : {} ",
                item.getReqId(),
                item.getMeta().getAggId(),
                item.getMeta().getPackageNr(),
                item.getMeta().getTotal());  
    }
    public  void update(Multiset agg){
       agg.forEach(l->log.info("update :  {}",l));
    }
    
    public  void info(List<Item> agg){
        
        agg.forEach(l->log.info("update :  {}",l));
     }

}
