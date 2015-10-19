package pl.java.scalatech.aggregate.generator;

import static com.google.common.collect.Maps.newHashMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import lombok.extern.slf4j.Slf4j;
import pl.java.scalatech.aggregate.domain.Item;
import pl.java.scalatech.aggregate.domain.ItemSupplier;
import pl.java.scalatech.aggregate.domain.MetaInformation;

@Slf4j
public class MessageGenerator {
    //TODO change on more flexible method
    public static List<Item> generateItem(int size) {
        List<Item> items = Stream.generate(ItemSupplier.INSTANCE).limit(size).collect(Collectors.toList());
        Multiset<Integer> counter = HashMultiset.create();
        Random r = new Random();
        for (Item item : items) {
            MetaInformation mi = new MetaInformation();
            if (r.nextBoolean()) {
                mi.setAggId("1:agg");
                counter.add(1);
                mi.setPackageNr(counter.count(1));
                mi.setTotal(5);
            } else {
                mi.setAggId("2:agg");
                counter.add(2);
                mi.setPackageNr(counter.count(2));
                mi.setTotal(3);
            }
            item.setMeta(mi);
        }
        return items;

    }

    BiFunction<Item, Integer, Item> decor = (t, u) -> {
        t.getMeta().setPackageNr(u);
        return t;
    };

    Function<Item, Item> ag = i -> i;

    @Test
    public void shouldGeneratorWork() {
        generateItem(8).stream().forEach(l -> log.info("{}", l));
    }

    @Test
    @Ignore
    public void shouldFilterOnly2Agg() {
        generateItem(8).stream().filter(i -> i.getMeta().getAggId().equals("1:agg")).forEach(l -> log.info("{}", l));
    }

}
