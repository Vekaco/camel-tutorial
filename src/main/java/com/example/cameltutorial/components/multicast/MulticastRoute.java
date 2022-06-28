package com.example.cameltutorial.components.multicast;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MulticastRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        AtomicInteger orderId = new AtomicInteger(100);
        from("timer:orders?period=2000")
                .process(exchange -> exchange.getIn()
                        .setBody("{order-id:'" +orderId.getAndIncrement() + "'," + "price:'$200'}"))
                .multicast()
                .to("direct:payment", "direct:stock-allocation");

        from("direct:payment")
                .process(exchange -> enrich(exchange, "Paid"))
                .log(LoggingLevel.ERROR, "${body}");

        from("direct:stock-allocation")
                .process(exchange -> enrich(exchange, "Allocated"))
                .log(LoggingLevel.ERROR, "${body}");
    }

    private void enrich(Exchange exchange, String statusValue) {
        Message in = exchange.getIn();
        String order = in.getBody(String.class);
        String status = "'status':'" + statusValue + "'";
        String body = order.replace("}", "," + status + "}");
        in.setBody(body);
    }
}
