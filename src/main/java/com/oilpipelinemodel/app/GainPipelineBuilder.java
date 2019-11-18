package com.oilpipelinemodel.app;

import java.util.ArrayList;
import java.util.List;

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

    public List<GainPipeLineProt> build() {
        // это ветвление, поэтому создаем 2 ветки сразу
        GainPipeLine gpL_top = new GainPipeLine();
        GainPipeLine gpL_down = new GainPipeLine();
        long BranchPositionTop;
        long BranchPositionDown;
        List<GainPipeLineProt> gplProt= new ArrayList<>();
          gplProt.add(new GainPipeLineProt());
          gplProt.add(new GainPipeLineProt());

        gpL_top.setDiam(diam);
        gpL_top.setNumBranch(this.numBranch);
        gpL_top.setDownObject(gpL_down);
        gpL_down.setDiam(diam);
        gpL_down.setNumBranch(this.numBranch+1);
        gpL_top.setTopObject(gpL_top);
        BranchPositionTop = mB.addPipeObject(gpL_top);
        BranchPositionDown = mB.addPipeObject(gpL_down);

          gplProt.get(0).setDiam(diam);
          gplProt.get(0).setNumBranch(numBranch);
          gplProt.get(0).setBranchPosition(BranchPositionTop);

          gplProt.get(1).setDiam(diam);
          gplProt.get(1).setNumBranch(numBranch+1);
          gplProt.get(1).setBranchPosition(BranchPositionDown);

        return gplProt;

    }
}
