package org.parison.cool.data;

/**
 * Created by Parison on 26/08/2017.
 */
public class UsineDivision {

    private String usine;
    private String division;

    public UsineDivision(String usine, String division) {
        this.usine = usine;
        this.division = division;
    }

    public String getUsine() {
        return usine;
    }

    public void setUsine(String usine) {
        this.usine = usine;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
