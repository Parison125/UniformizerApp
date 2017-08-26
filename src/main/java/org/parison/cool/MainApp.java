package org.parison.cool;

import org.apache.camel.main.Main;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @Parison Rvmld
 */
public class MainApp {


    public static void main(String... args) throws Exception {

/*        InputStream excelFile = ClassLoader.getSystemResource("tartanpion.txt").openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(excelFile));
        String line ="";
        while ((line = reader.readLine()) != null ) {
            System.out.println(line);
        }*/

        String file = "etc/tartanpion.txt";
        System.out.print("Fichier a lire "+file);
        File iz = new File(file);
        FileOutputStream fileout = new FileOutputStream(iz);
        fileout.write("Contenu qojfokqopcekc".getBytes());
        fileout.flush();
        fileout.close();
    }

}

