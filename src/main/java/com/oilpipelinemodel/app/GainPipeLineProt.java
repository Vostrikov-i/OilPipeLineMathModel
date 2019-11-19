package com.oilpipelinemodel.app;

import java.util.List;

public class GainPipeLineProt {
    private int numBranch;
    private double diam;
    private long BranchPosition; // положение объекта не ветке, этот параметр и номер ветки (numBranch ожднозначно определяют положение элемента в массиве)
    private ICalculatedPipeObject linkedObject; // связанный объект нефтепровода с данным прототипом
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
    public List<Double> getPressure(){return linkedObject.getCurrPressure(); } //вернули давление связанного объекта
    public List<Double> getValocity(){return linkedObject.getCurrVelocity();} // вернули скорость связанного объекта
    /*non public!!! */void setBranchPosition(long BranchPosition) {this.BranchPosition=BranchPosition;}
    /*non public!!!*/void setLinkedObject(ICalculatedPipeObject linkedObject){this.linkedObject=linkedObject;}

}
