package pl.java.scalatech.error;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class LogErrorTest {

    @Test(expected=CamelExecutionException.class)
    public void shouldLogErrorPolicyWork() throws Exception {
        ModelCamelContext context = new DefaultCamelContext();
        ProducerTemplate pt = context.createProducerTemplate();
        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                errorHandler(loggingErrorHandler().logName("myCamel").level(LoggingLevel.ERROR));
                from("seda:start").log(LoggingLevel.INFO,"myCamel","==== ${body}").throwException(new IllegalArgumentException("test body")).transform(constant("ok"));
            }
        });
        context.start();
        String result = (String) pt.requestBody("seda:start", "test");
        Assertions.assertThat(result).isEqualTo("ok");
        Thread.sleep(1000);
        context.stop();
    }
}
