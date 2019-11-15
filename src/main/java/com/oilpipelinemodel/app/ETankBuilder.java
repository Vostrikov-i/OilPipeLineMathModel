package com.oilpipelinemodel.app;

import com.oilpipelinemodel.app.prototype.TankProt;

public class ETankBuilder extends aPipeObjectBuilder {

    private double diam=0.2;
    private double height=10;
    private final long segmentLenght=10; // константы, т.к в целом разбивать более чем на 1 сегмент есть смысл только для нефтепровода, но длина нужна, для приведения рассчетов к реальному времени
    private final long lenght=10;
    private MagistralBuilder mB;

    // конструктор скрыть вне пакета
    ETankBuilder(MagistralBuilder mB)
    {
        this.mB=mB;
    }

    public void setDiam(double diam) {
        this.diam=diam;
    }
    // метод создания по образцу
    public void createByProt(TankProt tProt){
        diam=tProt.getDiam();
        height=tProt.getHeight();
    }
    @Override
    public void commit() {
        EndTank et=new EndTank();
        et.setHeightTank(this.height);
        et.SetDiam(this.diam);
        et.SetPipeLen(lenght);
        et.SetSegmentLen(segmentLenght);
        mB.addPipeObject(et);
    }

    public void setHeight(double height){
        this.height=height;
    }
}
