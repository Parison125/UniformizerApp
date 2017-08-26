package org.parison.cool;

import java.io.*;
import org.apache.log4j.Logger;

/**
 * @Parison Rvmld
 */
public class MainApp {

    final static Logger LOGGER = Logger.getLogger(MainApp.class);

    public static void main(String... args) throws Exception {

        LOGGER.debug("Beginning application execution ");
        File theseExcel = new File("etc/these.xlsx");

        if ( theseExcel.exists()) {
            FileInputStream fileIn = new FileInputStream(theseExcel);
            LOGGER.debug(" Creating input stream from " + theseExcel.getPath());
            CheckService checkService = new CheckService(fileIn);
        }
        else {
            LOGGER.warn("Le fichier "+theseExcel.getPath()+" n'existe pas");
            System.exit(1);
        }

    }

}

