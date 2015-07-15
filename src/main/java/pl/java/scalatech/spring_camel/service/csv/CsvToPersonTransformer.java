package pl.java.scalatech.spring_camel.service.csv;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Component;

import pl.java.scalatech.spring_camel.beans.Person;

import com.google.common.collect.Lists;

@Component
public class CsvToPersonTransformer {
    public List<Person> convert(List<List<String>> csvRows) {
        List<Person> results = Lists.newArrayList();
        for (List<String> csvRow : csvRows) {
            Person person = new Person();
            person.setClientNr(csvRow.get(0));
            person.setFirstName(csvRow.get(1));
            person.setLastName(csvRow.get(2));
            LocalDateTime ldt = LocalDateTime.parse(csvRow.get(3), DateTimeFormatter.ofPattern("yyyy-Mm-dd->HH:mm:ss", Locale.getDefault()));
            Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            person.setOrderDate(out);
            results.add(person);
        }
        return results;
    }
}