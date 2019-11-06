package com.oilpipelinemodel.app;

public class STankBuilder extends aPipeObjectBuilder {

    private double diam=0.2;
    private double height=10;
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

    @Override
    public void commit() {
        StartTank st=new StartTank();
        st.setHeightTank(this.height);
        st.SetDiam(this.diam);
        st.SetPipeLen(lenght);
        st.SetSegmentLen(segmentLenght);
        mB.addPipeObject(st);
    }

    public void setHeight(double height){
        this.height=height;
    }

}
