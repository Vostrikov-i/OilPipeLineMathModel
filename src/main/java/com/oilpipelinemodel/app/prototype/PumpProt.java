package com.oilpipelinemodel.app.prototype;

//


public class PumpProt {
    private double diam;
    private double maxSpeed;
    private double coeffA;
    private double coeffB;
    private int numBranch;


    // Getter
    public double getDiam() {
        return diam;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }
    public double getCoeffA() {
        return coeffA;
    }
    public int getNumBranch(){
        return numBranch;
    }

    public double getCoeffB() {
        return coeffB;
    }


    // Setter


    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setDiam(double diam) {
        this.diam = diam;
    }

    public void setCoeffA(double coeffA) {
        this.coeffA = coeffA;
    }

    public void setCoeffB(double coeffB) {
        this.coeffB = coeffB;
    }
    public void setNumBranch(int numBranch){
        this.numBranch=numBranch;
    }
}
