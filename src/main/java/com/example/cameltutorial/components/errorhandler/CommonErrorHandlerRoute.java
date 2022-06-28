package com.example.cameltutorial.components.errorhandler;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class CommonErrorHandlerRoute extends RouteBuilder {

    public final static AtomicInteger COUNTER = new AtomicInteger(1);
    @Override
    public void configure() throws Exception {

        from("direct:exceptionHandler")
                .log(LoggingLevel.WARN, "In Exception Handler")
                //.process(e-> SECONDS.sleep(5))
                .log(LoggingLevel.WARN, "${body}");
    }
}
