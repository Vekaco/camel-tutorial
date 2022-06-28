package com.example.cameltutorial.components.file;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileHandlerRoute2 extends RouteBuilder {
    private static final String FROM_DIR = "/Users/fukaikai/IdeaProjects/camel-tutorial/input_box/?noop=true&";
    private static final String TO_DIR = "/Users/fukaikai/IdeaProjects/camel-tutorial/output_box/?";
    private static final String APPEND = "&fileExist=Append";
    @Override
    public void configure() throws Exception {
        System.out.println("Handling file in camel...");
        //file move or file copy with noop=true option
        from("file://" + FROM_DIR + "fileName=camel-demo-in.txt")
                .to("file://" + TO_DIR + "fileName=camel-demo-out.txt");


        //append content to existing file...
        from("direct:appendToFile")
                .convertBodyTo(String.class)
                .process(Exchange::getMessage)
                .to("file://" + TO_DIR + "fileName=camel-demo-appends.txt" + APPEND);
    }
}
