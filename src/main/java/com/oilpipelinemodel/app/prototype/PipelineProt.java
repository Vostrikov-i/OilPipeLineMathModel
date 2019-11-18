package com.oilpipelinemodel.app.prototype;

public class PipelineProt {

    private double diam;
    private long lenght;
    private long segmentLen;
    private int numBranch;


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
}
