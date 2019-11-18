package com.oilpipelinemodel.app.prototype;

public class DamperProt {
    private double diam;
    private double coeffA;
    private int numBranch;

    //Getter
    public double getDiam() {
        return diam;
    }
    public double getCoeffA() {
        return coeffA;
    }
    public int getNumBranch() {
        return numBranch;
    }

    //Setter
    public void setNumBranch(int numBranch) {
        this.numBranch = numBranch;
    }
    public void setDiam(double diam) {
        this.diam = diam;
    }
    public void setCoeffA(double coeffA) {
        this.coeffA = coeffA;
    }

}