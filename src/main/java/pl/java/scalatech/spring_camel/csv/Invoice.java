package pl.java.scalatech.spring_camel.csv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CsvRecord(separator = ",", crlf = "\n", skipFirstLine = true)
public class Invoice {

    @DataField(pos = 1)
    private long id;

    @DataField(pos = 2)
    private double debit;

    @DataField(pos = 3)
    private double credit;
}
