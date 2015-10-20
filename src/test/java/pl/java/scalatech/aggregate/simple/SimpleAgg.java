package pl.java.scalatech.aggregate.simple;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleAgg implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        /*
         * if (oldExchange != null) {
         * log.info("OLD :  {}", oldExchange.getIn().getBody());
         * }
         * log.info("NEW  :  {}", newExchange.getIn().getBody());
         */
        List<String> result = newArrayList();
        String id = newExchange.getIn().getBody(String.class);
        saveIntoDb(id);
        if (oldExchange == null) {
            result.add(id);
            newExchange.getIn().setBody(result);
            return newExchange;
        }
        result = oldExchange.getIn().getBody(List.class);
        result.add(id);
        return oldExchange;
    }

    private void saveIntoDb(String id) {
        log.info("insert into db  :  {} ", id);
    }

    public void info(List<String> agg) {
        log.info("update ids : {}", agg);

    }

}
