package org.parison.cool;

import org.apache.camel.main.Main;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import org.apache.log4j.Logger;

/**
 * @Parison Rvmld
 */
public class MainApp {

    final static Logger LOGGER = Logger.getLogger(MainApp.class);

    public static void main(String... args) throws Exception {

/*       InputStream excelFile = ClassLoader.getSystemResource("tartanpion.txt").openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(excelFile));
        String line ="";
        while ((line = reader.readLine()) != null ) {
            System.out.println(line);
        }*/


        LOGGER.debug(" Distance = "+StringUtils.getLevenshteinDistance("","CC-11-Etiquettes"));
        LOGGER.debug(" Distance = "+StringUtils.getLevenshteinDistance("CC-10etiquete","CC-11-Prince"));
        LOGGER.debug("done");
       /* String file = "etc/tartanpion.txt";
        System.out.print("Fichier a lire "+file);
        File iz = new File(file);
        FileOutputStream fileout = new FileOutputStream(iz);
        fileout.write("Contenu qojfokqopcekc".getBytes());
        fileout.flush();
        fileout.close();*/
    }

}

