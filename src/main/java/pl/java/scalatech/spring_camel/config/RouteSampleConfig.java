package pl.java.scalatech.spring_camel.config;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Supplier;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

@Configuration
@ComponentScan(basePackages = "pl.java.scalatech.spring_camel.route", includeFilters = { @Filter(Component.class) })
public class RouteSampleConfig {

    private static Random random = new Random();
    private static HashFunction hf = Hashing.md5();

    @Bean
    @Scope(value = "prototype")
    public String mySimpleBean() {
        return "my hash: " + Hashing.sha256().newHasher().putInt(random.nextInt(100)).hash().toString();
    }

    @Bean
    public Supplier<LocalDateTime> currentTime() {
        return () -> LocalDateTime.now();
    }

    @Bean
    RouteBuilder exampleRoute(Supplier<LocalDateTime> currentTime) {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:exampleRoute").setHeader("curTime", method(currentTime)).log("${header[now]} - Hello, ${body}!!");
            }
        };
    }
}
