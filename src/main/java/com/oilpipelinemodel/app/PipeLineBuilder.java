package com.oilpipelinemodel.app;

/*
* Класс конкретного строителя для трубы нефтепровода
* Может строить как обычный строитель по шагам и потом собирать готовый объект
* Может строить по образцу, методом createbyProt - подается образец и на основании его полей заполняются поля создаваемого объекта
*
* TODO Возможно вызвать метод создания по образцу а потом вызывать Setter методы и тогда параметры образца не применятся, возможно стоит сделать что createByProt
*
* */
public class PipeLineBuilder {


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

    // метод создания по образцу
    public void createByProt(PipelineProt pProt){
        diam=pProt.getDiam();
        lenght=pProt.getLength();
        segmentLen=pProt.getSegmentLen();
    }



    public PipelineProt build()
    {
        PipeLine pL=new PipeLine();
        PipelineProt pProt=new PipelineProt(); //класс фасад для объекта
        long BranchPosition;
        pL.setDiam(diam);
        pL.setSegmentLen(segmentLen);
        pL.setPipeLen(lenght);
        pL.setNumBranch(numBranch);
        BranchPosition = mB.addPipeObject(pL);
          pProt.setDiam(diam);
          pProt.setSegmentLen(segmentLen);
          pProt.setLength(lenght);
          pProt.setNumBranch(numBranch);
          pProt.setBranchPosition(BranchPosition);
          pProt.setLinkedObject(pL);
        return pProt;
    }

}
