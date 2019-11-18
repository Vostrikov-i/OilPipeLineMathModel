package com.oilpipelinemodel.app;

public class DamperProt {
    private double diam;
    private double coeffA;
    private int numBranch;
    private long BranchPosition; // положение объекта не ветке, этот параметр и номер ветки (numBranch ожднозначно определяют положение элемента в массиве)

    //Getter
    public double getDiam() { return diam; }
    public double getCoeffA() { return coeffA; }
    public int getNumBranch() {
        return numBranch;
    }
    public long getBranchPosition() {return BranchPosition;}


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
    /*non public!!! */void setBranchPosition(long BranchPosition) {this.BranchPosition=BranchPosition;}
}
