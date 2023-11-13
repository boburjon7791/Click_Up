package com.example.demo.configuration;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyApplicationListener implements ApplicationListener<WebServerInitializedEvent> {
    public String hostName;
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        hostName = event.getWebServer().toString()+":"+event.getWebServer().getPort();
        System.out.println("hostName = " + hostName);
    }
}
