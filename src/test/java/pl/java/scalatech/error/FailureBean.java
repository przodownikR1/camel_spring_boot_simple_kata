package pl.java.scalatech.error;

import java.util.Map;

import org.apache.camel.Headers;

public class FailureBean {

    public void enrich(@Headers Map headers, Exception cause) {
        String failure = "The message failed because " + cause.getMessage();
        headers.put("FailureMessage", failure);
    }
}