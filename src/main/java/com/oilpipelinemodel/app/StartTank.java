package com.oilpipelinemodel.app;

import java.util.ArrayList;

class StartTank extends aPipeObject implements ISheduled
{
    private double HeightTank;
    private IConnectedPipeObject left_object=null;
    private IConnectedPipeObject right_object=null;

    public StartTank() {
        this.HeightTank=10;
        this.setDiam(0.6);
        this.setSegmentLen(50);
        this.setPipeLen(800);
    }

    void setHeightTank(double heightTank) {
        HeightTank = heightTank;
    }

    private void CalcPressPoint(int numPoint)
    {
        double dz_m;
        double dx_m;
        double Fi_m;
        double J_m;
        double p_curr;
        double v_curr;
        double d_t=new Long(this.getPeriod()).doubleValue();

        this.setPOldNeg(numPoint,this.getPressCurrNegValue(numPoint));
        this.setPOldPos(numPoint,this.getPressCurrPosValue(numPoint));

        this.setVOldNeg(numPoint,this.getVelCurrNegValue(numPoint));
        this.setVOldPos(numPoint,this.getVelCurrNegValue(numPoint));

            dz_m=this.getZValue(numPoint+1)-this.getZValue(numPoint);
            dx_m =(d_t/1000)*this.getSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
            Fi_m=(this.getLambda()*this.getDensity()*this.getVelOldNegValue(numPoint+1)*Math.abs(this.getVelOldNegValue(numPoint+1)));
            Fi_m=Fi_m/(2*this.getDiam()+this.getDensity()*this.g*(dz_m/dx_m)); // просто потому что не влазило в одну строку
            J_m=(this.getPressOldNegValue(numPoint+1)-(this.getDensity()*this.getSpeedWave()*this.getVelOldNegValue(numPoint+1)))+dx_m*Fi_m;

            p_curr=this.getDensity()*this.g*(this.HeightTank-this.getZValue(numPoint)); // вычисляем текущее давление
            v_curr=(p_curr-J_m)/(this.getDensity()*this.getSpeedWave());

        // Обновим текущего значения
        this.setPCurrNeg(numPoint,p_curr);
        this.setPCurrPos(numPoint,p_curr);
        //Обновим значения текущего цикла
        this.setVCurrNeg(numPoint,v_curr);
        this.setVCurrPos(numPoint,v_curr);

        this.setPressure(numPoint-1,p_curr/1000000);
        this.setVelocity(numPoint-1,v_curr);

        //System.out.println("Tank: Point "+(numPoint)+" Current Pressure: " + (this.GetPressCurr_p()/1000000) + " Current Velocity "+ this.GetVelCurr_p() );

    }
    @Override
    public void CalсPressure()
    {

        //перекладка данных для рассчета от правого объекта
        if (right_object!=null) {
            int len= getPressOldNeg().size();
            if (len>1) {
                setPOldNeg(len-1, right_object.getInPressure_neg());
            }

            len= getPressOldPos().size();
            if (len>1) {
                setPOldPos(0, right_object.getInPressure_pos());
            }

            len= getVelOldNeg().size();
            if (len>1) {
                setVOldNeg(0, right_object.getInVelocity_neg());
            }

            len= getVelOldPos().size();
            if (len>1) {
                setVOldPos(0, right_object.getInVelocity_pos());
            }
        }

        for (int i = 1; (i<this.getCntSegments()+1); i++)
        {
            CalcPressPoint(i);
        }



    }

    @Override
    public boolean isRun() {
        return false;
    }

    @Override
    public Double getInPressure_pos() {
        if (getPressOldPos().size()>1) {
            return getPressOldPosValue(1);
        } else {return null;}
    }

    @Override
    public Double getInPressure_neg() {
        if (getPressOldNeg().size()>1) {
            return getPressOldNegValue(1);
        } else {return null;}
    }

    @Override
    public Double getInVelocity_pos() {
        if (getVelOldPos().size()>1) {
            return getVelOldPosValue(1);
        } else {return null;}
    }

    @Override
    public Double getInVelocity_neg() {
        if (getVelOldNeg().size()>1) {
            return getVelOldNegValue(1);
        } else {return null;}
    }

    @Override
    public Double getOutPressure_pos() {
        int len= getPressOldPos().size();
        if (len>1) {return getPressOldPosValue(len-2);} else {return null; }
    }

    @Override
    public Double getOutPressure_neg() {
        int len= getPressOldNeg().size();
        if (len>1) {return getPressOldNegValue(len-2);} else {return null; }
    }

    @Override
    public Double getOutVelocity_pos() {
        int len= getVelOldPos().size();
        if (len>1) {return getVelOldPosValue(len-2);} else {return null; }
    }

    @Override
    public Double getOutVelocity_neg() {
        int len= getVelOldNeg().size();
        if (len>1) {return getVelOldNegValue(len-2);} else {return null; }
    }
    @Override
    public IConnectedPipeObject getLeftObject() {
        return left_object;
    }

    @Override
    public IConnectedPipeObject getRightObject() {
        return right_object;
    }

    @Override
    public void setRightObject(IConnectedPipeObject rO) {
        right_object=rO;
    }

    @Override
    public void setLeftObject(IConnectedPipeObject lO) {
        left_object=lO;
    }

    @Override
    public int getNumBranch() {
        return this.getNumberBranch();
    }


    @Override
    public ArrayList<Double> getCurrVelocity() {
        return null;
    }

    @Override
    public ArrayList<Double> getCurrPressure() {
        return null;
    }
}
