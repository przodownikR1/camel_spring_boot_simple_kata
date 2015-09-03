package pl.java.scalatech.delayer;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DelayBean {
    public int delay() {
        Random random = new Random();
        int r= random.nextInt(1000);
        log.info("delay :  {}",r);
        return r;
    }
}