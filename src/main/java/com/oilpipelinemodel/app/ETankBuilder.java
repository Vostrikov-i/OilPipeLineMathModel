package com.oilpipelinemodel.app;

public class ETankBuilder {

    private double diam=0.2;
    private double height=10;
    private final long segmentLenght=10; // константы, т.к в целом разбивать более чем на 1 сегмент есть смысл только для нефтепровода, но длина нужна, для приведения рассчетов к реальному времени
    private final long lenght=10;
    private MagistralBuilder mB;
    private int numBranch=0;


    // конструктор скрыть вне пакета
    ETankBuilder(MagistralBuilder mB)
    {
        this.mB=mB;
    }

    public void setDiam(double diam) {
        this.diam=diam;
    }
    public void setHeight(double height){
        this.height=height;
    }
    public void setNumBranch(int numBranch) {
        this.numBranch = numBranch;
    }

    // метод создания по образцу
    public void createByProt(TankProt tProt){
        diam=tProt.getDiam();
        height=tProt.getHeight();
        numBranch=tProt.getNumBranch();
    }
    public TankProt build() {
        EndTank et=new EndTank();
        TankProt tProt = new TankProt();
        long BranchPosition;
        et.setHeightTank(this.height);
        et.setDiam(this.diam);
        et.setPipeLen(lenght);
        et.setSegmentLen(segmentLenght);
        et.setNumBranch(numBranch);
        BranchPosition = mB.addPipeObject(et);
          tProt.setDiam(diam);
          tProt.setHeight(height);
          tProt.setNumBranch(numBranch);
          tProt.setBranchPosition(BranchPosition);
          tProt.setLinkedObject(et);
        return tProt;
    }


}
