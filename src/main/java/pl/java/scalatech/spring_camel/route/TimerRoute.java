package pl.java.scalatech.spring_camel.route;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimerRoute extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        //      from("timer:test?period=1s")
        from("quartz2://myGroup/przodownik?cron=*+*+10-18+?+*+MON-FRI").transform().simple("ref:mySimpleBean")
                .process(exchange -> log.info("+++ timer {}", LocalDateTime.now())).to("log:pl.java.scalatech?level=INFO");
    }

}
