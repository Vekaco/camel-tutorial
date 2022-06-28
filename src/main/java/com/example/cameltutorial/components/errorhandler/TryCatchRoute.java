package com.example.cameltutorial.components.errorhandler;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TryCatchRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:time?period=1000").process(
                        exchange -> exchange.getIn().setBody(new Date())
                )
                .doTry().bean(HelloBean.class, "callBad")
                .doCatch(Exception.class).to("direct:exceptionHandler")
                .end()
                .log(LoggingLevel.INFO, ">> ${header.firedTime} >> ${body}")
                .to("log:reply");
    }
}
