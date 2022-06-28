package com.example.cameltutorial.components.jms;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JMSRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jms:queue:orders")
                .log(LoggingLevel.ERROR, "Got a message ${body}");
    }
}
