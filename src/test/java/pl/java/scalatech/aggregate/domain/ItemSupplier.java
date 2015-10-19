package pl.java.scalatech.aggregate.domain;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

import java.util.function.Supplier;

import org.apache.commons.lang.RandomStringUtils;

public class ItemSupplier implements Supplier<Item>{
 public static final ItemSupplier INSTANCE = new ItemSupplier();
   
  
    private  ItemSupplier() {
 
    }
    
    @Override
    public Item get() {
        return Item.builder().reqId(RandomStringUtils.randomNumeric(3)).name(randomAlphabetic(10)).build();
    }

}
