package com.example.cameltutorial.components.aggregator;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class AggregatorRoute extends RouteBuilder {
    final String CORRELATION_ID = "correlationId";
    @Override
    public void configure() throws Exception {
        Random random = new Random();

        from("timer:insurance?period=200")
                .process(exchange -> {
                    Message message = exchange.getMessage();
                    message.setHeader(CORRELATION_ID, random.nextInt(4));
                    exchange.getMessage().setBody(new Date());

                })
                .aggregate(header(CORRELATION_ID), new MyAggregationStrategy())
                .completionSize(3)
                .log(LoggingLevel.ERROR, "${header." + CORRELATION_ID +"} ${body}");

    }
}
