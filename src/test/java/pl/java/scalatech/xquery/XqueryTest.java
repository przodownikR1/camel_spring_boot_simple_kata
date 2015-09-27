package pl.java.scalatech.xquery;

import static org.apache.camel.component.xquery.XQueryBuilder.xquery;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import pl.java.scalatech.CommonCreateCamelContext;

public class XqueryTest extends CommonCreateCamelContext {
    @Test
    public void shouldXquery() throws Exception {
        createContextWithGivenRoute(new MyRouteBuilder(), 2000);

    }

    class MyRouteBuilder extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("file://src/main/resources/?noop=true&fileName=bookStore.xml").to("direct:start");

            from("direct:start")
                    .transform(xquery("<books>{ for $x in /bookstore/book " + "where $x/price>30 order by $x/title " + "return $x/title }</books>")).log(
                            LoggingLevel.INFO, "myCamel", "${body}");
        }

    }

    class XqueryParamRouteBuilder extends RouteBuilder {
        @Override
        public void configure() throws Exception {
            from("direct:xqpStart")
                    .transform(
                            xquery("declare variable $in.headers.myParamValue as xs:integer external; <books value='{$in.headers.myParamValue}'>{for $x in /bookstore/book where $x/price>$in.headers.myParamValue order by $x/title return $x/title}</books>"));
        }
    }

}
