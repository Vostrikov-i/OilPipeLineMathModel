package com.oilpipelinemodel.app;

import com.oilpipelinemodel.app.prototype.*;

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

    public void build() {
        // это ветвление, поэтому создаем 2 ветки сразу
        DrainPipeLine   dpL_top = new DrainPipeLine();
        DrainPipeLine  dpL_down = new DrainPipeLine();
        dpL_top.SetDiam(diam);
        dpL_top.setNumBranch(this.numBranch);
        dpL_top.setDownObject(dpL_down);
        dpL_down.SetDiam(diam);
        dpL_down.setNumBranch(this.numBranch+1);
        dpL_top.setTopObject(dpL_top);
        mB.addPipeObject(dpL_top);
        mB.addPipeObject(dpL_down);
    }
}
