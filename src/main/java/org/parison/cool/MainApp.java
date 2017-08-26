package org.parison.cool;

import java.io.*;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.parison.cool.data.UsineDivision;

/**
 * @Parison Rvmld
 */
public class MainApp {

    final static Logger LOGGER = Logger.getLogger(MainApp.class);

    public static void main(String... args) throws Exception {

        LOGGER.debug("Beginning application execution ");
        File theseExcel = new File("etc/these.xlsx");
        if ( theseExcel.exists()) {
            InputStream fileIn = new FileInputStream(theseExcel);
            LOGGER.debug(" Creating input stream from " + theseExcel.getPath());
            CheckService checkService = new CheckService(fileIn);
            checkService.processExcelFile();
            //fileIn.close();
        }
        else {
            LOGGER.warn("Le fichier "+theseExcel.getPath()+" n'existe pas");
            System.exit(1);
        }

    }

}

