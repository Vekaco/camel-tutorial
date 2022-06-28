package com.example.cameltutorial.components;

import com.example.cameltutorial.components.choice.ChoiceRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.Map;

@MockEndpoints
public class ChoiceRouteTest extends CamelTestSupport {
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ChoiceRoute();
    }

    @Test
    void givenGadgetOrderRequest_route_WillProcessGadgetOrder() {
        String body = "Airpods";
        Map headers = Maps.newHashMap("inventory", "gadget");
        template.sendBodyAndHeaders("direct:orders", body, headers);
    }

    @Test
    void givenWidgetOrderRequest_route_WillProcessWidgetOrder() {
        String body = "Amazon";
        Map headers = Maps.newHashMap("inventory", "widget");
        template.sendBodyAndHeaders("direct:orders", body, headers);
    }


}
