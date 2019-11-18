package com.oilpipelinemodel.app;

class DamperBuilder {

    private MagistralBuilder mB;
    private double diam;
    private double coeffA;
    private int numBranch=0;

    // конструктор скрыть вне пакета
    DamperBuilder(MagistralBuilder mB)
    {
        this.mB=mB;
    }

    public void setDiam(double diam) {
       this.diam=diam;
    }
    public void setApproxCoeffA(double coeffA){
       this.coeffA=coeffA;
    }
    public void setNumBranch(int numBranch){this.numBranch=numBranch;}

    // метод создания по образцу
    public void createByProt(DamperProt dProt){
        diam=dProt.getDiam();
        coeffA=dProt.getCoeffA();
        numBranch=dProt.getNumBranch();
    }

    public DamperProt build() {
        Damper dm=new Damper();
        DamperProt dProt = new DamperProt();
        long BranchPosition;
        dm.setApprox_a(coeffA);
        dm.setDiam(diam);
        BranchPosition = mB.addPipeObject(dm);
          dProt.setDiam(diam);
          dProt.setCoeffA(coeffA);
          dProt.setNumBranch(numBranch);
          dProt.setBranchPosition(BranchPosition);
        return dProt;
    }

}

