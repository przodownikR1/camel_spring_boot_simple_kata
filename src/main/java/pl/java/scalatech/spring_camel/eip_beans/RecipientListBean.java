package pl.java.scalatech.spring_camel.eip_beans;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;
//@Component
public class RecipientListBean {

	private  List<String> uris;

	public RecipientListBean(String... uris) {
		this.uris = Arrays.asList(uris);
	}
    @Handler
	public List<String> route(Exchange exchange) {
		return uris;
	}

}