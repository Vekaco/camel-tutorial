package com.example.cameltutorial.components;

import lombok.Builder;
import lombok.Data;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import java.util.*;

public class SplitterRouteTest extends CamelTestSupport {

    @Test
    void splitEip() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:split");
        mock.expectedBodiesReceived("A", "B", "C");
        //List<String> body = Arrays.asList("A", "B", "C");
        template.sendBody("direct:start", "A#B#C");
        assertMockEndpointsSatisfied();
    }

    @Test
    void complexSplitEip() {
        List<Order> orders = new ArrayList<>();
        orders.add(Order.builder().orderId("0100").itemIds(Arrays.asList("I100", "I101", "I102")).build());
        orders.add(Order.builder().orderId("0200").itemIds(Arrays.asList("I200", "I201", "I202")).build());
        CustomerOrders customerOrders = CustomerOrders.builder().customerId("Tom").orders(orders).build();
        template.sendBody("direct:customerOrder", customerOrders);
    }

    @Test
    void splitAndAggregateEip() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:aggregatedResult");
        mock.expectedBodiesReceived("A=Apple + B=Bucket + C=Cat");
        template.sendBody("direct:customerOrderAggregate", "A,B,C");
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .log("origin: ${body}")
                        .split(body())
                        //.split(body(), "#")
                        //.split(body()).delimiter("#")
                        .log("Split line ${body}")
                        .to("mock:split");

                from("direct:customerOrder")
                        .log("Customer Id: ${body.customerId}")
                        .log("orders: ${body.orders}")
                        //.split(simple("${body.orders}"))
                        .split(method(OrderServices.class, "getOrders"))
                        .log("order-> ${body}");


                from("direct:customerOrderAggregate")
                        .log("${body}")
                        .split(body(), new WordAggregationStrategy()).stopOnException()
                        .bean(WordTranslateBean.class).to("mock:split")
                        .end()
                        .log("Aggregated ${body}")
                        .to("mock:aggregatedResult")
                ;


            }
        };
    }


    @Data
    @Builder
    static class CustomerOrders  {
        private String customerId;
        private List<Order> orders;
    }

    @Data
    @Builder
    static class Order {
        private String orderId;
        private List<String> itemIds;
    }

    private static class OrderServices {
         static List<Order> getOrders(CustomerOrders customerOrders) {
            return customerOrders.getOrders();
        }
    }
    public class WordAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange;
            }
            String body = newExchange.getIn().getBody(String.class).trim();
            String existing = oldExchange.getIn().getBody(String.class).trim();
            oldExchange.getIn().setBody(existing + " + " + body);
            return oldExchange;
        }
    }

//    public class WordAggregationStrategy implements AggregationStrategy {
//
//        @Override
//        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
//            if (oldExchange == null) {
//                return newExchange;
//            }
//
//            String body = newExchange.getIn().getBody(String.class).trim();
//            String existing = oldExchange.getIn().getBody(String.class).trim();
//            oldExchange.getIn().setBody(existing + "+" + body);
//            return oldExchange;
//        }
//    }
//

    public static class WordTranslateBean {

        private static Map<String, String> words = new HashMap<String, String>();

        public WordTranslateBean() {
            words.put("A", "Apple");
            words.put("B", "Bucket");
            words.put("C", "Cat");
        }

        public static String translate(String key) {
            if (!words.containsKey(key)) {
                throw new IllegalArgumentException("Key not a known word " + key);
            }
            return key + "=" + words.get(key);
        }
    }
//    public class WordTranslateBean {
//        private Map<String, String> map = new HashMap<>();
//
//        public WordTranslateBean() {
//            map.put("A", "Apple");
//            map.put("B", "Bucket");
//            map.put("C", "Cat");
//        }
//
//        public String translate(String key) {
//            if(!map.containsKey(key)) {
//                throw new RuntimeException("key not valid");
//            }
//            return key + "=" + map.get(key);
//        }
//    }
}
