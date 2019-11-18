package com.oilpipelinemodel.app;

import com.oilpipelinemodel.app.prototype.nReturnValveProt;

// строитель для обратного клапана
public class notReturnValveBuilder {
    private MagistralBuilder mB;
    private double diam=0.2;
    private int numBranch=0;
    // константы, потому что нет смысла в разбиении обратного клапана на более чем 1 сегмент, длину сегмента  и самого нефтепровода выбрали минимальную
    private final long lenght=1;
    private final long segment_lenght=1;


    notReturnValveBuilder(MagistralBuilder mB){
        this.mB=mB;
    }



    // метод создания по образцу
    public void createByProt(nReturnValveProt nrvProt){
        diam=nrvProt.getDiam();
    }

    public void setDiam(double diam){
        this.diam=diam;
    }

    void setNumBranch(int numBranch) { // обязательно закрытый внутри пакета
        this.numBranch = numBranch;
    }


    void build() {
        nRetrunValve nRv=new nRetrunValve();
        nRv.SetDiam(this.diam);
        nRv.setNumBranch(this.numBranch);
        nRv.SetPipeLen(lenght);
        nRv.SetSegmentLen(segment_lenght);
        mB.addPipeObject(nRv);
    }

}
