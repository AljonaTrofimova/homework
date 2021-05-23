package com.swedbank.webservice.homework;

import com.swedbank.webservice.homework.config.ApplicationConfig;
import com.swedbank.webservice.homework.service.WebService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        WebService webService = context.getBean(WebService.class);
        webService.getContentFromUrl("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY");
    }
}
