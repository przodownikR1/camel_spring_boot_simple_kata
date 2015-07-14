package pl.java.scalatech.spring_camel.service.csv;

import java.util.Date;
import java.util.Random;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import pl.java.scalatech.spring_camel.beans.Person;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

@Component
public class CsvService {
    private static Random random = new Random();
    private static HashFunction hf = Hashing.md5();
    @EndpointInject
    ProducerTemplate producer;

    public void createCsvRecord() {
        producer.sendBody("direct:csv",
                Person.builder().clientNr(Hashing.sha256().newHasher().putInt(random.nextInt(100)).hash().toString()).firstName("slawek").lastName("borowiec")
                        .orderDate(new Date()).build());
    }
}
