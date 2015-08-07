package pl.java.scalatech.spring_camel.eip_beans;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import pl.java.scalatech.spring_camel.beans.Customer;

@Component
@Slf4j
public class MessageBean {
	    public String read(Customer msg) {
	        log.info("+++ messageBean :  {}",msg);      
	        return msg + ":" + Thread.currentThread();
	    }
	}