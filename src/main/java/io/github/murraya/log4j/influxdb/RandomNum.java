package io.github.murraya.log4j.influxdb;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

class RandomNum implements Serializable {

    private final ThreadLocalRandom random;

    private RandomNum() {
        random = ThreadLocalRandom.current();
    }

    private static RandomNum instance;

    protected static RandomNum getInstance() {
        if(instance == null) {
            instance = new RandomNum();
        }
        return instance;
    }
    protected String next256String(){
        return String.valueOf(random.nextInt(256));
    }
}
