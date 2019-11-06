package com.oilpipelinemodel.app;
/*
* Класс конкретного строителя для трубы нефтепровода
*
*
* */
public class PipeLineBuilder extends aPipeObjectBuilder {


    private MagistralBuilder mB;
    private double diam=0.2;
    private long lenght=50;
    private long segmentLen=50;
    private int numBranch=0;

    // конструктор скрыть вне пакета
    PipeLineBuilder(MagistralBuilder mB)
    {
        this.mB=mB;
    }




    public void setDiam(double diam) {
        this.diam=diam;
    }
    public void setLenght(long lenght){
        this.lenght=lenght;
    }
    public  void setSegmentLen(long segmentLen){
        this.segmentLen=segmentLen;
    }
    // Не public !!!!!!
    void setNumBranch(int numBranch){
        this.numBranch=numBranch;
    }

    @Override
    public void commit()
    {
        PipeLine pL=new PipeLine();
        pL.SetDiam(diam);
        pL.SetSegmentLen(segmentLen);
        pL.SetPipeLen(lenght);
        pL.setNumBranch(numBranch);
        mB.addPipeObject(pL);
    }

}
