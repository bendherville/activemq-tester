package com.activemqtester.mvc;

import com.activemqtester.camel.CamelRoute;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/trigger")
public class Trigger {

    @Produce(uri = CamelRoute.STEP_1_URI)
    private ProducerTemplate producer;

    @RequestMapping(value = "/camel", method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public String triggerCamelRoute(
            @RequestParam(value = "sleep", defaultValue = "0") int sleepInMS,
            @RequestParam(value = "failOnFirstAttempt", defaultValue = "false") boolean failOnFirstAttempt) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("failOnFirstAttempt", failOnFirstAttempt);
        headers.put("refId", UUID.randomUUID().toString());

        producer.sendBodyAndHeaders(sleepInMS, headers);

        return "Camel trigger: " + sleepInMS;
    }
}
