package org.parison.cool;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.parison.cool.data.Defaut;
import org.parison.cool.data.FamilleQualite;
import org.parison.cool.data.UsineDivision;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

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
                    row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    String usine = row.getCell(1).getStringCellValue().trim();
                    String division = row.getCell(2).getStringCellValue().trim();
                    if (usine.equals("")|| usine == null || division.equals("")
                            || division == null || usine.equals("NA") || division.equals("NA"))
                    {
                        continue;
                    }
                    UsineDivision trueUsineDiv = checkUsineDivision(usine, division);
                    row.getCell(1).setCellValue(trueUsineDiv.getUsine());
                    row.getCell(2).setCellValue(trueUsineDiv.getDivision());

                    //checking date
                    String date = row.getCell(3).getStringCellValue().trim();
                    if (date.equals("") || date == null || date.equals("NA"))
                    {
                        continue;
                    }
                    String trueDate = checkDate(date);
                    row.getCell(3).setCellValue(trueDate);

                    //Checking Famille qualité
                    String familleQualite = row.getCell(5).getStringCellValue().trim();
                    if (familleQualite.equals("") || familleQualite == null || familleQualite.equals("NA"))
                    {
                        continue;
                    }
                    String trueFamilleQualite = checkFamilleQualite(familleQualite);
                    row.getCell(5).setCellValue(trueFamilleQualite);

                    //Checking defauts
                    String defaut = row.getCell(7).getStringCellValue().trim();
                    if (defaut.equals("") || defaut == null || defaut.equals("NA"))
                    {
                        continue;
                    }
                    String trueDefauts = checkDefauts(defaut);
                    row.getCell(7).setCellValue(trueDefauts);

                    //break;
                }

                i++;
            }
            fileIn.close();

            LOGGER.debug("Writing changes into theseModifie.xlsx");
            FileOutputStream outFile =new FileOutputStream(new File("etc/theseModifie.xlsx"));
            workbook.write(outFile);
            LOGGER.debug("Done writing.");
            outFile.close();

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } catch ( Exception e) {
            e.printStackTrace();
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
        Map<String,String> dictMois = new HashMap<>();
        dictMois.put("Janvier","Janvier");
        dictMois.put("Janv","Janvier");
        dictMois.put("Jan","Janvier");
        dictMois.put("Février","Février");
        dictMois.put("Fév","Février");
        dictMois.put("Fev","Février");
        dictMois.put("Févr","Février");
        dictMois.put("Mars","Mars");
        dictMois.put("Avril","Avril");
        dictMois.put("Avr","Avril");
        dictMois.put("Avr","Avril");
        dictMois.put("Mai","Mai");
        dictMois.put("Juin","Juin");
        dictMois.put("Jun","Juin");
        dictMois.put("Juillet","Juillet");
        dictMois.put("Jui","Juillet");
        dictMois.put("Aout","Aout");
        dictMois.put("Septembre","Septembre");
        dictMois.put("Sep","Septembre");
        dictMois.put("Octobre","Octobre");
        dictMois.put("Oct","Octobre");
        dictMois.put("Octbr","Octobre");
        dictMois.put("Novembre","Novembre");
        dictMois.put("Nov","Novembre");
        dictMois.put("Décembre","Décembre");
        dictMois.put("Déc","Décembre");
        dictMois.put("Dec","Décembre");

        LOGGER.debug("Date en entrée "+date);
        for (String key : dictMois.keySet()) {
            if (date.toLowerCase().equals(dictMois.get(key).toLowerCase())) {
                result = dictMois.get(key) ;
                LOGGER.debug(" iter 1 - Date result "+date);
            }
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


    public String checkFamilleQualite (String fqParameter) throws FileNotFoundException {

        String result = null;
        Gson gson = new Gson();
        String[] splitedParameters = fqParameter.split("-");
        FamilleQualite[] familleQualiteList = gson.fromJson(new FileReader("etc/familleQualite.json"), FamilleQualite[].class);

        if (splitedParameters.length == 1) {
            for (FamilleQualite familleQualite : familleQualiteList) {
                if (splitedParameters[0].toLowerCase().equals(familleQualite.getName().toLowerCase()) ) {
                    result = familleQualite.getFullName();
                    LOGGER.debug("Iter2 - found result = "+result);
                    return  result;
                }

            }

            if (result == null){
                return fqParameter;
            }
        }
        String entryParameter = splitedParameters[0]+"-"+splitedParameters[1]+"-"+splitedParameters[2];
        entryParameter = entryParameter.toLowerCase().trim();
        String entryParameterCodeNumber = splitedParameters[2];

        LOGGER.debug("EntryParameter "+entryParameter);
        LOGGER.debug("Entry code "+entryParameterCodeNumber);
        for (FamilleQualite familleQualite : familleQualiteList) {
            String familleCode = familleQualite.getCode().trim().toLowerCase();
            if (entryParameter.equals(familleCode)) {
                result = familleQualite.getFullName();
                LOGGER.debug("Iter1 - found result = "+result);
                break;
            }

        }

        if (result == null) {

            for (FamilleQualite familleQualite : familleQualiteList) {
                String familleCode = familleQualite.getCodeNumber();
                if (entryParameterCodeNumber.equals(familleCode) || Integer.valueOf(entryParameterCodeNumber) == Integer.valueOf(familleCode)) {
                    result = familleQualite.getFullName();
                    LOGGER.debug("Iter2 - found result = "+result);
                    break;
                }

            }

        }

        return result;
    }


    public String checkDefauts(String defaut) throws FileNotFoundException {

        String result = null;
        Gson gson = new Gson();
        Defaut[] defautList = gson.fromJson(new FileReader("etc/defauts.json"), Defaut[].class);
        String entryParameter = defaut.trim().toLowerCase();
        //Performing levenshtein distance algorithm to compute similarity between words

        LOGGER.debug("Parameter in entry "+entryParameter);
        float distancePrecedente = 0;
        String tmpResult = "";
        int i = 0;
        for ( Defaut defautItem : defautList) {
            if( i == 0 ) {
                distancePrecedente = StringUtils.getLevenshteinDistance(entryParameter,defautItem.getValeurDefaut().trim().toLowerCase());
                LOGGER.debug("Distance between \""+entryParameter+"\" and \""+defautItem.getValeurDefaut().trim().toLowerCase()+"\" "+distancePrecedente);
                tmpResult = defautItem.getValeurDefaut();
            }
            if ( i > 0 ) {
                float distanceAct = StringUtils.getLevenshteinDistance(entryParameter,defautItem.getValeurDefaut().trim().toLowerCase());
                LOGGER.debug("Distance between \""+entryParameter+"\" and \""+defautItem.getValeurDefaut().trim().toLowerCase()+"\" "+distanceAct);
                if (distanceAct < distancePrecedente ) {
                    distancePrecedente = distanceAct;
                    tmpResult = defautItem.getValeurDefaut();
                }
            }
            i++;

        }
        LOGGER.debug("Result \""+tmpResult+"\"");
        result = tmpResult;
        return result;
    }


}
