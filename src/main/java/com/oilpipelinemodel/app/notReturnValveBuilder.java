package com.oilpipelinemodel.app;

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
        numBranch=nrvProt.getNumBranch();
    }

    public void setDiam(double diam){
        this.diam=diam;
    }
    void setNumBranch(int numBranch) { // обязательно закрытый внутри пакета
        this.numBranch = numBranch;
    }


   public nReturnValveProt build() {
        nRetrunValve nRv=new nRetrunValve();
        nReturnValveProt nrvProt=new nReturnValveProt(); //класс фасад для объекта
        long BranchPosition;
        nRv.setDiam(this.diam);
        nRv.setNumBranch(this.numBranch);
        nRv.setPipeLen(lenght);
        nRv.setSegmentLen(segment_lenght);
        nRv.setNumBranch(numBranch);
        BranchPosition=mB.addPipeObject(nRv);
         nrvProt.setDiam(this.diam);
         nrvProt.setNumBranch(this.numBranch);
         nrvProt.setBranchPosition(BranchPosition);
         nrvProt.setLinkedObject(nRv);
       return nrvProt;
    }

}
