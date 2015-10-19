package pl.java.scalatech.aggregate.domain;

import java.util.function.BiFunction;

public class ItemAddMetaConsumer implements BiFunction<MetaInformation,Item,Item>{

    @Override
    public Item apply(MetaInformation t, Item u) {
        u.setMeta(t);
        return u;
    }
}
