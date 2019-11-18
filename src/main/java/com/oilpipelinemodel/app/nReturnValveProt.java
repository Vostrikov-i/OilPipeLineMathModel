package com.oilpipelinemodel.app;

public class nReturnValveProt {
    private double diam=0.2;
    private int numBranch;
    private long BranchPosition; // положение объекта не ветке, этот параметр и номер ветки (numBranch ожднозначно определяют положение элемента в массиве)

    //Getter
    public double getDiam() {
        return diam;
    }
    public int getNumBranch() {
        return numBranch;
    }
    public long getBranchPosition() {return BranchPosition;}


    //Setter
    public void setDiam(double diam) {
        this.diam = diam;
    }
    public void setNumBranch(int numBranch) {
        this.numBranch = numBranch;
    }
    /*non public!!! */void setBranchPosition(long BranchPosition) {this.BranchPosition=BranchPosition;}
}
