package com.example.cameltutorial.components.aggregator;

import org.apache.camel.Exchange;

import java.util.Objects;

public class MyAggregationStrategy implements org.apache.camel.AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

        if (Objects.isNull(oldExchange)) {
            return newExchange;
        }

        String oldBody = oldExchange.getIn().getBody(String.class);
        String newBody = newExchange.getIn().getBody(String.class);

        String aggregatedBody = oldBody + "->" + newBody;

        oldExchange.getIn().setBody(aggregatedBody);
        return oldExchange;
    }
}
