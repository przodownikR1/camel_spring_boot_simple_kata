package pl.java.scalatech.spring_camel.csv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryInvoice {
    private double credit;
    private double debit;
    private double saldo;
}
