package com.oilpipelinemodel.app;
/*
* Строитель ветвлений не должен использоваться вне пакета!
* Используется только внутри строителей групп насосов и групп заслонок
* TODO продумать механизм ветвления нефтепровода без дальнейшего его сужения, например сделать несколько параллельных веток нефтепровода
* */
class DrainPipelineBuilder {
    private double diam=0.2;
    private DrainGroupBuilder dgB;

    private MagistralPipeline mP;

    DrainPipelineBuilder(DrainGroupBuilder dgB) {
        this.dgB = dgB;
    }
    void setDiam(double diam)
    {
        this.diam=diam;
    }

    void commit() {
        DrainPipeLine dpL = new DrainPipeLine();
        dpL.SetDiam(this.diam);
        dgB.addBranchedObject(dpL);


    }
}
