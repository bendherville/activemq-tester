package com.activemqtester.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/resource/hc")
public class HealthCheck {

    @RequestMapping(value = "/application", method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public String applicationHealthCheck() {
        return "application status: ok";
    }
}
