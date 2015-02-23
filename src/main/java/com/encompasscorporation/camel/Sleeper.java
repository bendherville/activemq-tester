package com.encompasscorporation.camel;

import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Sleeper {

    private static final Logger LOG = LoggerFactory.getLogger(Sleeper.class);

    public void execute(Integer sleepInMS, @Header("refId") String refId) throws InterruptedException {
        LOG.debug("{}: Sleeping...", refId);
        Thread.sleep(sleepInMS);
        LOG.debug("{}: ... awake", refId);
    }
}
