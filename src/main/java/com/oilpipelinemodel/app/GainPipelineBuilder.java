package com.oilpipelinemodel.app;

import com.oilpipelinemodel.app.GainPipeLine;
import com.oilpipelinemodel.app.MagistralBuilder;
import com.oilpipelinemodel.app.prototype.*;

public class GainPipelineBuilder {

    private MagistralBuilder mB;
    private double diam=0.2;
    private int numBranch=0;

    GainPipelineBuilder(MagistralBuilder mB) {
        this.mB = mB;
    }

    public void setDiam(double diam) { this.diam=diam; }
    public void setNumBranch(int numBranch){this.numBranch=numBranch;}

    public void createByProt(GainPipeLineProt gplProt){
        diam=gplProt.getDiam();
        numBranch=gplProt.getNumBranch();
    }

    public void build() {
        // это ветвление, поэтому создаем 2 ветки сразу
        GainPipeLine gpL_top = new GainPipeLine();
        GainPipeLine gpL_down = new GainPipeLine();
        gpL_top.SetDiam(diam);
        gpL_top.setNumBranch(this.numBranch);
        gpL_top.setDownObject(gpL_down);
        gpL_down.SetDiam(diam);
        gpL_down.setNumBranch(this.numBranch+1);
        gpL_top.setTopObject(gpL_top);
        mB.addPipeObject(gpL_top);
        mB.addPipeObject(gpL_down);
    }
}
