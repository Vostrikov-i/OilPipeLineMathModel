package com.oilpipelinemodel.app;

public class GainPipelineBuilder extends aPipeObjectBuilder {

    private GainPipeLine gpL;
    private GainGroupBuilder ggB;
    private double diam=0.2;

    GainPipelineBuilder(GainGroupBuilder ggB) {
        this.ggB = ggB;
    }
    void setDiam(double diam)
    {
        this.diam=diam;
    }

    @Override
    void commit() {
        gpL = new GainPipeLine();
        gpL.SetDiam(diam);
        ggB.addBranchedObject(gpL);
    }
}
