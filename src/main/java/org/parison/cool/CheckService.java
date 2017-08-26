package org.parison.cool;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Parison on 26/08/2017.
 */
public class CheckService {

    final static Logger LOGGER = Logger.getLogger(CheckService.class);

    FileInputStream fileIn;

    public CheckService(FileInputStream fileIn) {
        this.fileIn = fileIn;
    }

    public void processExcelFile() {
        LOGGER.debug("Beginning process Excel File");

    }
}
