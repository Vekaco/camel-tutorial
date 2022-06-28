package com.example.cameltutorial.components.seda;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class SedaRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:ping?period=200")
                .process(exchange -> {
                    Message message = new DefaultMessage(exchange);
                    message.setBody(new Date());
                    exchange.setMessage(message);
                })
                .to("seda:weightShifter");

        from("seda:weightShifter?multipleConsumers=true")
                .to("direct:complexProcess");


        from("direct:complexProcess")
                .log(LoggingLevel.ERROR, "${body}")
                .process(exchange -> SECONDS.sleep(2))
                .end();

    }
}
