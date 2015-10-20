package pl.java.scalatech.spiltter.clazz;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import lombok.Data;

public class Wrapper {

 
    public static List createList(){
        return newArrayList("slawek","tomek","kalina","maciek");
    }
    
}
