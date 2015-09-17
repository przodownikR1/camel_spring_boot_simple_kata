package pl.java.scalatech.spring_camel.csv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SummaryInvoice {
    private long id;
    private double credit;
    private double debit;
    private double saldo;
    
    
}
