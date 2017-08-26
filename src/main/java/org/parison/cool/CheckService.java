package org.parison.cool;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.parison.cool.data.UsineDivision;

import java.io.*;

/**
 * Created by Parison on 26/08/2017.
 */
public class CheckService {

    final static Logger LOGGER = Logger.getLogger(CheckService.class);

    InputStream fileIn;

    public CheckService(InputStream fileIn) {
        this.fileIn = fileIn;
    }

    public void processExcelFile() throws IOException {
        LOGGER.debug("Beginning process Excel File");
        Workbook workbook = null;
        try {

            workbook =  WorkbookFactory.create(fileIn);
            Sheet sheet = workbook.getSheetAt(0);
            Row row1 = sheet.getRow(1);
            int i = 0;

            for (Row row : sheet) {
                if (i<2)
                {
                    i++;
                    continue;
                }
                //Checking usine and division
                String usine = row.getCell(1).getStringCellValue();
                String division = row.getCell(2).getStringCellValue();

            }

            fileIn.close();

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } catch ( Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
        }
    }

    public UsineDivision checkUsineDivision (String usine, String division) throws FileNotFoundException {

        Gson gson = new Gson();
        UsineDivision[] staff = gson.fromJson(new FileReader("etc/UsineDivision.json"), UsineDivision[].class);
        for ( UsineDivision usineDivision: staff) {
            String tmpDivision = usineDivision.getDivision().toLowerCase();
            String tmpUsine = usineDivision.getUsine().toLowerCase();

            if( tmpDivision.equals(division.toLowerCase()) || tmpUsine.equals(usine.toLowerCase())) {
                return usineDivision;
            } else if ( StringUtils.getLevenshteinDistance(tmpDivision.toLowerCase(),division.toLowerCase())<= 3 ) {

            }

        }

        return null;
    }
}
