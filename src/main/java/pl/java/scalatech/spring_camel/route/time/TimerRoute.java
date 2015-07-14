package pl.java.scalatech.spring_camel.route.time;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("timer")
@Slf4j
public class TimerRoute extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("quartz2://myGroup/przodownik?cron=0/10+*+10-18+?+*+MON-FRI").transform().simple("ref:mySimpleBean")
                .process(exchange -> log.info("+++ timer {}", LocalDateTime.now())).to("log:pl.java.scalatech?level=INFO");
    }

}
