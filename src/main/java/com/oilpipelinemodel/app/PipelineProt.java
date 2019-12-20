package com.oilpipelinemodel.app;

import java.util.List;

public class PipelineProt {

    private double diam;
    private long lenght;
    private long segmentLen;
    private int numBranch;
    private long BranchPosition; // положение объекта не ветке, этот параметр и номер ветки (numBranch ожднозначно определяют положение элемента в массиве)
    private ICalculatedPipeObject linkedObject; // связанный объект нефтепровода с данным прототипом
    //Getter
    public double getDiam() {
        return diam;
    }
    public long getLength() {
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
    public void setLength(long lenght) {
        this.lenght = lenght;
    }
    public void setSegmentLen(long segmentLen) {
        this.segmentLen = segmentLen;
    }
    public void setNumBranch(int numBranch){
        this.numBranch=numBranch;
    }
    public List<Double> getPressure(){return linkedObject.getCurrPressure(); } //вернули давление связанного объекта
    public List<Double> getVelocity(){return linkedObject.getCurrVelocity();} // вернули скорость связанного объекта
    /*non public!!! */void setBranchPosition(long BranchPosition) {this.BranchPosition=BranchPosition;}
    /*non public!!!*/void setLinkedObject(ICalculatedPipeObject linkedObject){this.linkedObject=linkedObject;}
}
