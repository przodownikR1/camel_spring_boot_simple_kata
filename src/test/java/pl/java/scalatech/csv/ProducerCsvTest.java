package pl.java.scalatech.csv;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;
import pl.java.scalatech.spring_camel.beans.Person;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class ProducerCsvTest extends CommonCreateCamelContext {
    private static Random random = new Random();
    private static HashFunction hf = Hashing.md5();

    @Test
    public void shouldProduceCsvFile() throws InterruptedException, Exception {
        // delete(new File("outbox"));
        createContextWithGivenRoute(new MyRouteBuilder(), 1000);

    }

    class MyRouteBuilder extends RouteBuilder {
        BindyCsvDataFormat bindy = new BindyCsvDataFormat("pl.java.scalatech.spring_camel.beans");

        @Override
        public void configure() throws Exception {
            ProducerTemplate pt = getContext().createProducerTemplate();
            from("file:inbox?noop=true&delay=5000").marshal(bindy).to("file://outbox");//?fileName=${header.count}.txt
           /*  from("direct:csv").marshal(bindy).log("Result : ${body}");
              pt.sendBody("direct:csv",
              Person.builder().clientNr(Hashing.sha256().newHasher().putInt(random.nextInt(100)).hash().toString()).firstName("slawek")
              .lastName("borowiec").orderDate(new Date()).build());*/
             

            //.setHeader("CamelFileName", constant("report.txt")).
        }
    }

    void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        /*
         * if (!f.delete())
         * throw new FileNotFoundException("Failed to delete file: " + f);
         */
    }
}
