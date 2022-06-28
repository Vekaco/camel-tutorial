package com.example.cameltutorial.components;

import com.example.cameltutorial.components.choice.ChoiceRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;


import java.util.Map;

import static org.apache.camel.builder.AdviceWith.adviceWith;
import static org.assertj.core.util.Maps.newHashMap;

@MockEndpoints
public class ChoiceRouteMockTest extends CamelTestSupport {

    private static final String GADGET = "gadget";
    private static final String WIDGET = "widget";
    private static final String GENERAL = "general";
    private static final String INVENTORY = "inventory";

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ChoiceRoute();
    }

    @Test
    void givenGadgetOrderRequest_route_WillProcessGadgetOrder() throws Exception {
        MockEndpoint gadget = mockEndpoint(GADGET, 1);
        MockEndpoint widget = mockEndpoint(WIDGET, 0);
        MockEndpoint general = mockEndpoint(GENERAL, 0);

        context.start();
        String body = "Airpods";
        Map headers = newHashMap(INVENTORY, GADGET);
        template.sendBodyAndHeaders("direct:orders", body, headers);

        assertAllSatisfied(gadget, widget, general);
    }

    @Test
    void givenWidgetOrderRequest_route_WillProcessWidgetOrder() throws Exception {
        MockEndpoint gadget = mockEndpoint(GADGET, 0);
        MockEndpoint widget = mockEndpoint(WIDGET, 1);
        MockEndpoint general = mockEndpoint(GENERAL, 0);

        context.start();
        String body = "Amazon";
        Map headers = newHashMap(INVENTORY, WIDGET);
        template.sendBodyAndHeaders("direct:orders", body, headers);

        assertAllSatisfied(gadget, widget, general);
    }

    @Test
    void givenGeneralOrderRequest_route_WillProcessGeneralOrder() throws Exception {
        MockEndpoint gadget = mockEndpoint(GADGET, 0);
        MockEndpoint widget = mockEndpoint(WIDGET, 0);
        MockEndpoint general = mockEndpoint(GENERAL, 1);

        context.start();
        String body = "dress";
        Map headers = newHashMap(INVENTORY, GENERAL);
        template.sendBodyAndHeaders("direct:orders", body, headers);

        assertAllSatisfied(gadget, widget, general);
    }

    private MockEndpoint mockEndpoint(String orderType, int expectedCount) throws Exception {
        RouteDefinition route = context.getRouteDefinition(orderType);
        adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddLast().to("mock:" + orderType);
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:" + orderType);
        mockEndpoint.expectedMessageCount(expectedCount);
        return mockEndpoint;
    }


    private void assertAllSatisfied(MockEndpoint... endpoints) throws InterruptedException {
        for (MockEndpoint endpoint: endpoints) {
            endpoint.assertIsSatisfied();
        }

    }
}
