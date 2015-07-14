package pl.java.scalatech.spring_camel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pl.java.scalatech.spring_camel.service.MyService;

@SpringBootApplication
public class SpringBootCamelApp implements CommandLineRunner {

    @Autowired
    private MyService myService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootCamelApp.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        myService.invokeRoutes();

    }

}
