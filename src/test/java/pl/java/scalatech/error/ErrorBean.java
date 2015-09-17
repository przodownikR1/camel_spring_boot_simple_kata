package pl.java.scalatech.error;

import org.apache.camel.Body;

public class ErrorBean {

    
    public String generateException(@Body String text) {
        throw new IllegalStateException("my Exception -state illegal");
        
    }
    
}
