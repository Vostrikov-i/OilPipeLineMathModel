package com.oilpipelinemodel.app;
// строитель для обратного клапана
public class notReturnValveBuilder extends aPipeObjectBuilder {
    private nRetrunValve nRv; // обратный клапан
    private double diam=0.2;
    private MagistralBuilder mB;
    private int numBranch=0;
    // константы, потому что нет смысла в разбиении обратного клапана на более чем 1 сегмент, длину сегмента  и самого нефтепровода выбрали минимальную
    private final long lenght=1;
    private final long segment_lenght=1;


    notReturnValveBuilder(MagistralBuilder mB){
        this.mB=mB;
    }

    public void setDiam(double diam){
        this.diam=diam;
    }

    void setNumBranch(int numBranch) { // обязательно закрытый внутри пакета
        this.numBranch = numBranch;
    }

    @Override
    void commit() {
        nRv=new nRetrunValve();
        nRv.SetDiam(this.diam);
        nRv.setNumBranch(this.numBranch);
        nRv.SetPipeLen(lenght);
        nRv.SetSegmentLen(segment_lenght);
        mB.addPipeObject(nRv);
    }

}
