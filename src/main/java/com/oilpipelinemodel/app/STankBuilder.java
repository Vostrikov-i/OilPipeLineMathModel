package com.oilpipelinemodel.app;

public class STankBuilder{

    private double diam=0.2;
    private double height=10;
    private int numBranch=0;
    private final long segmentLenght=10; // константы, т.к в целом разбивать более чем на 1 сегмент есть смысл только для нефтепровода, но длина нужна, для приведения рассчетов к реальному времени
    private final long lenght=10;
    private MagistralBuilder mB;

    // конструктор скрыть вне пакета
    STankBuilder(MagistralBuilder mB)
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
        StartTank st=new StartTank();
        TankProt tProt=new TankProt();
        long BranchPosition;
        st.setHeightTank(height);
        st.setDiam(diam);
        st.setPipeLen(lenght);
        st.setSegmentLen(segmentLenght);
        st.setNumBranch(numBranch);
        BranchPosition = mB.addPipeObject(st);
          tProt.setHeight(height);
          tProt.setDiam(diam);
          tProt.setNumBranch(numBranch);
          tProt.setBranchPosition(BranchPosition);
          tProt.setLinkedObject(st);
        return tProt;
    }



}
