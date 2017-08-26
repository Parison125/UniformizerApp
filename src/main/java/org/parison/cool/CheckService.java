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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            int i = 0;

            for (Row row : sheet) {
                if ( i >= 1) {

                    //Checking usine and division
                    String usine = row.getCell(1).getStringCellValue().trim();
                    String division = row.getCell(2).getStringCellValue().trim();
                    UsineDivision trueUsineDiv = checkUsineDivision(usine, division);

                    //checking date
                    String date = row.getCell(3).getStringCellValue().trim();
                    String trueDate = checkDate(date);

                    //Checking Famille qualité
                    String familleQualite = row.getCell(5).getStringCellValue().trim();

                    break;
                }

                i++;
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
        UsineDivision[] reference = gson.fromJson(new FileReader("etc/UsineDivision.json"), UsineDivision[].class);
        UsineDivision result = null;
        LOGGER.debug("Usine / division couple to check : "+usine + " / "+ division);
        for ( UsineDivision usineDivision: reference) {
            String tmpUsine = usineDivision.getUsine().toLowerCase().trim();

            LOGGER.debug("Testing "+usine.toLowerCase()+" with "+tmpUsine+" => "+usine.toLowerCase().equals(tmpUsine));
            if( usine.toLowerCase().equals(tmpUsine)) {
                LOGGER.debug("iter1 - Result returned "+usineDivision.getUsine()+" / "+usineDivision.getDivision());
                result = usineDivision;
                break;
            }
        }

        if (result == null) {

            for (UsineDivision usineDivision : reference) {

                String tmpUsine = usineDivision.getUsine().toLowerCase();

                float maxUsineLength = (tmpUsine.length() > usine.length() ? tmpUsine.length() : usine.length());
                float usineSimilarity = (maxUsineLength - Integer.parseInt(String.valueOf(StringUtils.getLevenshteinDistance(usine.toLowerCase(), tmpUsine)))) / maxUsineLength;
                LOGGER.debug("Similarity percentage between "+usine+" and "+tmpUsine+" : "+usineSimilarity);

                if ( usineSimilarity >= 0.80 ) {
                    LOGGER.debug("iter2 - Result returned "+usineDivision.getUsine()+" / "+usineDivision.getDivision());
                    result = usineDivision;
                    break;
                }

            }
        }

        return result;
    }


    public String checkDate (String date) {

        String result = null;
        List<String> listMois = new ArrayList<>(Arrays.asList("Janvier","Février","Mars","Avril","Mai","Juin","Juillet",
                "Août","Septembre","Octobre","Novembre","Décembre"));

        LOGGER.debug("Date en entrée "+date);
        if (listMois.contains(date)) {
            result = date;
            LOGGER.debug(" iter 1 - Date result "+date);
        }

        if (result == null ) {

            String  moisPrec = "";
            float distPrec = 0;
            int i = 0;
            for( String mois : listMois ) {

                if ( i == 0 ) {
                    distPrec = Integer.parseInt(String.valueOf(StringUtils.getLevenshteinDistance(date,mois)));
                    LOGGER.debug("Distance between "+date+" and "+mois+" "+distPrec);
                    moisPrec = mois ;
                }
                if ( i > 0 ) {
                    float distanceAct = Integer.parseInt(String.valueOf(StringUtils.getLevenshteinDistance(date,mois)));
                    LOGGER.debug("Distance between "+date+" and "+mois+" "+distanceAct);
                    if (distanceAct < distPrec ) {
                        distPrec = distanceAct;
                        moisPrec = mois;
                    }
                }
                i++;
            }
            result = moisPrec ;
            LOGGER.debug(" iter 2 - Date result "+moisPrec);

        }

        return result;
    }


    public String checkFamilleQualite(String familleQualite) {


        return null;
    }
}
