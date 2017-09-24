package org.parison.cool;

import java.io.*;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Parison Rvmld
 */
public class MainApp {


    public static void main(String... args) throws Exception {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");
        new Fenetre(appContext);

    }

}

