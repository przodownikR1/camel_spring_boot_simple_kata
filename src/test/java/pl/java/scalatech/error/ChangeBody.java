package pl.java.scalatech.error;

import org.apache.camel.Body;

public class ChangeBody {

    
    public String change(@Body String str) {
        return "change  :: "+str; 
    }
    
}
