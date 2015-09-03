package pl.java.scalatech.spring_camel.csv;

import lombok.Data;

@Data
public class SummaryInvoice {

    private long invoiceId;
    private double credit;
    private double debit;
    private double saldo;
}
