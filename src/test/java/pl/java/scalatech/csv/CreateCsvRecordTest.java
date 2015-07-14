package pl.java.scalatech.csv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.spring_camel.config.CamelConfig;
import pl.java.scalatech.spring_camel.service.csv.CsvService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CamelConfig.class)
@ActiveProfiles("test")
public class CreateCsvRecordTest {
    @Autowired
    private CsvService csvService;

    @Test
    public void shouldRecordCreate() {
        csvService.createCsvRecord();
    }

}
