package com.oilpipelinemodel.app;

class DamperBuiler extends aPipeObjectBuilder {

    private MagistralBuilder mB;
    private double diam;
    private double coeffA;


    // конструктор скрыть вне пакета
    DamperBuiler(MagistralBuilder mB)
    {
        this.mB=mB;
    }

    void setDiam(double diam) {
       this.diam=diam;
    }

    void setApproxCoeffA(double coeffA){
       this.coeffA=coeffA;
    }
    @Override
    void commit() {
        Damper dm=new Damper();
        dm.setApprox_a(this.coeffA);
        dm.SetDiam(this.diam);
        mB.addPipeObject(dm);
    }

}

