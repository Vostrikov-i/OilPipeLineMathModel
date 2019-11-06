package com.oilpipelinemodel.app;

import java.util.ArrayList;

class StartTank extends aPipeObject implements ISheduled
{
    private double HeightTank;
    private IConnectedPipeObject left_object=null;
    private IConnectedPipeObject right_object=null;

    public StartTank() {
        this.HeightTank=10;
        this.SetDiam(0.6);
        this.SetSegmentLen(50);
        this.SetPipeLen(800);
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
        double d_t=new Long(this.GetPeriod()).doubleValue();

        this.SetP_old_neg(numPoint,this.GetPress_curr_negValue(numPoint));
        this.SetP_old_pos(numPoint,this.GetPress_curr_posValue(numPoint));

        this.SetV_old_neg(numPoint,this.GetVel_curr_negValue(numPoint));
        this.SetV_old_pos(numPoint,this.GetVel_curr_negValue(numPoint));

            dz_m=this.GetZValue(numPoint+1)-this.GetZValue(numPoint);
            dx_m =(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
            Fi_m=(this.GetLambda()*this.GetDensity()*this.GetVel_old_negValue(numPoint+1)*Math.abs(this.GetVel_old_negValue(numPoint+1)));
            Fi_m=Fi_m/(2*this.GetDiam()+this.GetDensity()*this.g*(dz_m/dx_m)); // просто потому что не влазило в одну строку
            J_m=(this.GetPress_old_negValue(numPoint+1)-(this.GetDensity()*this.GetSpeedWave()*this.GetVel_old_negValue(numPoint+1)))+dx_m*Fi_m;

            p_curr=this.GetDensity()*this.g*(this.HeightTank-this.GetZValue(numPoint)); // вычисляем текущее давление
            v_curr=(p_curr-J_m)/(this.GetDensity()*this.GetSpeedWave());

        // Обновим текущего значения
        this.SetP_curr_neg(numPoint,p_curr);
        this.SetP_curr_pos(numPoint,p_curr);
        //Обновим значения текущего цикла
        this.SetV_curr_neg(numPoint,v_curr);
        this.SetV_curr_pos(numPoint,v_curr);

        this.setPressure(numPoint-1,p_curr/1000000);
        this.setVelocity(numPoint-1,v_curr);

        //System.out.println("Tank: Point "+(numPoint)+" Current Pressure: " + (this.GetPressCurr_p()/1000000) + " Current Velocity "+ this.GetVelCurr_p() );

    }
    @Override
    public void CalсPressure()
    {

        //перекладка данных для рассчета от правого объекта
        if (right_object!=null) {
            int len=GetPress_old_neg().size();
            if (len>1) {
                SetP_old_neg(len-1, right_object.getInPressure_neg());
            }

            len=GetPress_old_pos().size();
            if (len>1) {
                SetP_old_pos(0, right_object.getInPressure_pos());
            }

            len=GetVel_old_neg().size();
            if (len>1) {
                SetV_old_neg(0, right_object.getInVelocity_neg());
            }

            len=GetVel_old_pos().size();
            if (len>1) {
                SetV_old_pos(0, right_object.getInVelocity_pos());
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
        if (GetPress_old_pos().size()>1) {
            return GetPress_old_posValue(1);
        } else {return null;}
    }

    @Override
    public Double getInPressure_neg() {
        if (GetPress_old_neg().size()>1) {
            return GetPress_old_negValue(1);
        } else {return null;}
    }

    @Override
    public Double getInVelocity_pos() {
        if (GetVel_old_pos().size()>1) {
            return GetVel_old_posValue(1);
        } else {return null;}
    }

    @Override
    public Double getInVelocity_neg() {
        if (GetVel_old_neg().size()>1) {
            return GetVel_old_negValue(1);
        } else {return null;}
    }

    @Override
    public Double getOutPressure_pos() {
        int len=GetPress_old_pos().size();
        if (len>1) {return GetPress_old_posValue(len-2);} else {return null; }
    }

    @Override
    public Double getOutPressure_neg() {
        int len=GetPress_old_neg().size();
        if (len>1) {return GetPress_old_negValue(len-2);} else {return null; }
    }

    @Override
    public Double getOutVelocity_pos() {
        int len=GetVel_old_pos().size();
        if (len>1) {return GetVel_old_posValue(len-2);} else {return null; }
    }

    @Override
    public Double getOutVelocity_neg() {
        int len=GetVel_old_neg().size();
        if (len>1) {return GetVel_old_negValue(len-2);} else {return null; }
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
