package com.oilpipelinemodel.app;

public class PumpProt {
    private double diam;
    private double maxSpeed;
    private double coeffA;
    private double coeffB;
    private int numBranch;
    private long BranchPosition; // положение объекта не ветке, этот параметр и номер ветки (numBranch ожднозначно определяют положение элемента в массиве)

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
    public long getBranchPosition() {return BranchPosition;}


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
    /*non public!!! */void setBranchPosition(long BranchPosition) {this.BranchPosition=BranchPosition;}
}
