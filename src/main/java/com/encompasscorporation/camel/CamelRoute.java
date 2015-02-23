package com.encompasscorporation.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.PredicateBuilder.and;

@Component
public class CamelRoute extends SpringRouteBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamelRoute.class);

    public static final String STEP_1_URI = "activemq:queue:step1?destination.consumer.prefetchSize=0";
    public static final String STEP_2_URI = "activemq:queue:step2?destination.consumer.prefetchSize=0";

    @Override
    public void configure() throws Exception {

        errorHandler(transactionErrorHandler().disableRedelivery());

        from(STEP_1_URI)
                .onException(Exception.class).handled(false).log(LoggingLevel.DEBUG, LOGGER, "${headers.refId}: Step 1 failed").end()
                .transacted("PROPAGATION_REQUIRES_NEW")
                .log(LoggingLevel.DEBUG, LOGGER, "${headers.refId}: Step 1 processing")
                .beanRef("sleeper")
                .to(STEP_2_URI)
                .choice()
                .when(and(header("failOnFirstAttempt").isEqualTo(true), header("JMSRedelivered").not().isEqualTo(true)))
                .throwException(new RuntimeException("Failing on first delivery attempt"))
                .end()
                .log(LoggingLevel.DEBUG, LOGGER, "${headers.refId}: Step 1 completed");

        from(STEP_2_URI)
                .onException(Exception.class).handled(false).log(LoggingLevel.DEBUG, LOGGER, "${headers.refId}: Step 2 failed").end()
                .transacted("PROPAGATION_REQUIRES_NEW")
                .log(LoggingLevel.DEBUG, LOGGER, "${headers.refId}: Step 2 completed");
    }
}
