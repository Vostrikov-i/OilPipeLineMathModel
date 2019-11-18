package com.oilpipelinemodel.app;

import java.util.ArrayList;
import java.util.List;

/*
* Строитель ветвлений не должен использоваться вне пакета!
* Используется только внутри строителей групп насосов и групп заслонок
* TODO продумать механизм ветвления нефтепровода без дальнейшего его сужения, например сделать несколько параллельных веток нефтепровода
* */
class DrainPipelineBuilder  {
    private double diam=0.2;
    private MagistralBuilder mB;
    private int numBranch=0;


    DrainPipelineBuilder(MagistralBuilder mB) {
        this.mB = mB;
    }

    public void setDiam(double diam)
    {
        this.diam=diam;
    }
    public void setNumBranch(int numBranch){this.diam=diam;}

    public void createByProt(DrainPipeLineProt dplProt){
        diam=dplProt.getDiam();
        numBranch=dplProt.getNumBranch();
    }

    public List<DrainPipeLineProt> build() {
        // это ветвление, поэтому создаем 2 ветки сразу
        DrainPipeLine dpL_top = new DrainPipeLine();
        DrainPipeLine dpL_down = new DrainPipeLine();
        long BranchPositionTop;
        long BranchPositionDown;
        List<DrainPipeLineProt> dplProt= new ArrayList<>();
          dplProt.add(new DrainPipeLineProt());
          dplProt.add(new DrainPipeLineProt());

        dpL_top.setDiam(diam);
        dpL_top.setNumBranch(numBranch);
        dpL_top.setDownObject(dpL_down);
        dpL_down.setDiam(diam);
        dpL_down.setNumBranch(numBranch+1);
        dpL_top.setTopObject(dpL_top);

        BranchPositionTop = mB.addPipeObject(dpL_top);
        BranchPositionDown = mB.addPipeObject(dpL_down);

          dplProt.get(0).setDiam(diam);
          dplProt.get(0).setNumBranch(numBranch);
          dplProt.get(0).setBranchPosition(BranchPositionTop);

          dplProt.get(1).setDiam(diam);
          dplProt.get(1).setNumBranch(numBranch+1);
          dplProt.get(1).setBranchPosition(BranchPositionDown);

        return dplProt;

    }
}
