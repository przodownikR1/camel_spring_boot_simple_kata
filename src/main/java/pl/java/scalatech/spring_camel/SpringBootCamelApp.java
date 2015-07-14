package pl.java.scalatech.spring_camel;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pl.java.scalatech.spring_camel.service.MyService;
import pl.java.scalatech.spring_camel.service.csv.CsvService;

@SpringBootApplication
@Slf4j
public class SpringBootCamelApp implements CommandLineRunner {

    @Autowired
    private MyService myService;

    @Autowired
    private CsvService csvService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootCamelApp.class, args);

    }

    @Override
    public void run(String... args) throws Exception {

        myService.invokeRoutes();
        csvService.createCsvRecord();

    }

}
