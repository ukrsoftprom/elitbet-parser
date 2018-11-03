package com.elitbet.parser;

import javafx.application.Platform;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;

public class Application {
    private final static Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args)  {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");
        Platform.setImplicitExit(false);
        {
            Parser createparser = (Parser) applicationContext.getBean("createparser");
            Thread createparserThread = new Thread(createparser);
            createparserThread.start();
        }
        {
            Parser updateparser = (Parser) applicationContext.getBean("updateparser");
            Thread updateparserThread = new Thread(updateparser);
            updateparserThread.start();
        }
    }
}