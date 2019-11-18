package com.oilpipelinemodel.app;

import com.oilpipelinemodel.app.prototype.DamperProt;

class DamperBuiler {

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

    // метод создания по образцу
    public void createByProt(DamperProt dProt){
        diam=dProt.getDiam();
        coeffA=dProt.getCoeffA();
    }

    void commit() {
        Damper dm=new Damper();
        dm.setApprox_a(this.coeffA);
        dm.SetDiam(this.diam);
        mB.addPipeObject(dm);
    }

}

