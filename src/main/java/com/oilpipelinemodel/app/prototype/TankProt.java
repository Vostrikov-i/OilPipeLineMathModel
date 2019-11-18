package com.oilpipelinemodel.app.prototype;

public class TankProt {
    private double diam=0.2;
    private double height=10;
    private int numBranch;

    public double getDiam() {
        return diam;
    }
    public double getHeight() {
        return height;
    }
    public int getNumBranch(){return numBranch;}



    public void setDiam(double diam) {
        this.diam = diam;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public void setNumBranch(int numBranch){this.numBranch=numBranch;}
}
