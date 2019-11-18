package com.oilpipelinemodel.app;

public class PipelineProt {

    private double diam;
    private long lenght;
    private long segmentLen;
    private int numBranch;
    private long BranchPosition; // положение объекта не ветке, этот параметр и номер ветки (numBranch ожднозначно определяют положение элемента в массиве)

    //Getter
    public double getDiam() {
        return diam;
    }
    public long getLenght() {
        return lenght;
    }
    public long getSegmentLen() {
        return segmentLen;
    }
    public int getNumBranch(){
        return numBranch;
    }
    public long getBranchPosition() {return BranchPosition;}


    //Setter
    public void setDiam(double diam) {
        this.diam = diam;
    }
    public void setLenght(long lenght) {
        this.lenght = lenght;
    }
    public void setSegmentLen(long segmentLen) {
        this.segmentLen = segmentLen;
    }
    public void setNumBranch(int numBranch){
        this.numBranch=numBranch;
    }
    /*non public!!! */void setBranchPosition(long BranchPosition) {this.BranchPosition=BranchPosition;}
}
