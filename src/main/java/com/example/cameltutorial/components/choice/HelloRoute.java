package com.example.cameltutorial.components.choice;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class HelloRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:greeting").id("greeting")
                .log(LoggingLevel.ERROR, "Hello ${body}")
                .choice()
                    .when().simple("${body} contains 'Team'")
                        .log(LoggingLevel.ERROR, "I like working with Teams")
                    .otherwise()
                        .log(LoggingLevel.ERROR, "Solo fighter :-)")
                    .end()
                .end();
    }
}
