package com.elitbet.parser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;

public class Application {
    private final static Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");
        Parser parser = (Parser) applicationContext.getBean("updateparser");
        Thread parserThread = new Thread(parser);
        parserThread.start();
        parserThread.join();
    }
}