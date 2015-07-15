package pl.java.scalatech.nbp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.stream.InputStreamCache;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.IOHelper;
import org.junit.Ignore;
import org.junit.Test;

import pl.java.scalatech.spring_camel.nbp.Pozycja;
import pl.java.scalatech.spring_camel.nbp.Tabela_kursow;

@Slf4j
public class NBPDownloader {
    static int  count = 0;
	@Test
	@Ignore
	public void marshallerTest() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(pl.java.scalatech.spring_camel.nbp.Tabela_kursow.class);
		Tabela_kursow tk = new Tabela_kursow();
		tk.setTyp("A");
		tk.setUid("11a187");
		tk.setNumer_tabeli("187/A/NBP/2011");
		tk.setData_publikacji(new Date());
		Pozycja p1 = new Pozycja();
		p1.setNazwa_waluty("bat (Tajlandia)");
		p1.setPrzelicznik("1");
		p1.setKod_waluty("THB");
		p1.setKurs_sredni("0,1053");
		Pozycja p2 = new Pozycja();
		p2.setNazwa_waluty("dolar amerykański");
		p2.setPrzelicznik("1");
		p2.setKod_waluty("USD");
		p2.setKurs_sredni("3,2563");
		tk.getPozycje().add(p1);
		tk.getPozycje().add(p2);

		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(tk, System.out);
	}

	@Test
	@Ignore
	public void unmarshallTest() throws JAXBException {
		File xmlFile = new File("src/test/resources/a187z110927.xml");
		JAXBContext jc = JAXBContext.newInstance(pl.java.scalatech.spring_camel.nbp.Tabela_kursow.class);
		Unmarshaller u = jc.createUnmarshaller();
		Tabela_kursow tk = (Tabela_kursow) u.unmarshal(xmlFile);
		log.info(tk.toString());

	}

	@Test
	 @Ignore
	public void nbpReaderTest() throws Exception {
		CamelContext context = new DefaultCamelContext();
			context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("http://nbp.pl/kursy/xml/a187z110927.xml?delay=1000").process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						
						log.info("################################");
						Map<String, Object> map = exchange.getIn().getHeaders();
						InputStreamCache obj = (InputStreamCache) exchange.getIn().getBody();	
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						IOHelper.copy(obj,bos);
						String xml = new String(bos.toByteArray());
						log.info("WYNIK NB P : " + count + "  "  + xml);
					    count++;
					}

				}).end();
			}

		});
		context.start();
		Thread.sleep(100);
		context.stop();
	}
	@Test
	 @Ignore
	public void nbpConsumerTest(){
		CamelContext context = new DefaultCamelContext();
		ConsumerTemplate ct= context.createConsumerTemplate();
		Exchange ex= ct.receive("http://nbp.pl/kursy/xml/a187z110927.xml");
		Message message = ex.getIn();
		log.info(message.getHeaders().toString());
	    

	}
	

}
