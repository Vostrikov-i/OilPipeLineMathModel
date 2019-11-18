package com.oilpipelinemodel.app;

public class GainPipeLineProt {
    private int numBranch;
    private double diam;
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
    public void setNumBranch(int numBranch) {
        this.numBranch = numBranch;
    }
    public void setDiam(double diam) {
        this.diam = diam;
    }
    /*non public!!! */void setBranchPosition(long BranchPosition) {this.BranchPosition=BranchPosition;}
}
