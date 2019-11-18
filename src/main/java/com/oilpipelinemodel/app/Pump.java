package com.oilpipelinemodel.app;

/*Насос
*
*
*
* */

import java.util.ArrayList;

class Pump extends aPipeObject implements ISheduled {

    private Double speed; // скорость вращения двигателя
    private Double approx_a=305.0; // коэффициент апроксимации функции напора от расхода
    private Double approx_b=0.00000208; // коэффициент апроксимации функции напора от расхода
    private Double max_speed=3000.0; // максимальная скорость вращения двигателя насоса
    private IConnectedPipeObject left_object=null;
    private IConnectedPipeObject right_object=null;

    public Double getFreq(){
        return this.speed;
    }

    public Double getApprox_a() {
        return approx_a;
    }

    public Double getApprox_b() {
        return approx_b;
    }

    public Double getMax_speed() {
        return max_speed;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public boolean setApprox_a(Double approx_a) {
        if (approx_a>0) {
            this.approx_a = approx_a;
            return true;
        }
        return false;
    }


    public boolean setApprox_b(Double approx_b) {
        if (approx_b>0) {
            this.approx_b = approx_b;
            return true;
        }
        return false;
    }

    public boolean setMax_speed(Double max_speed) {
        if(max_speed>0) {
            this.max_speed = max_speed;
            return true;
        }
        return false;
    }


    private void CalcPressPoint(int numPoint) {

        double dz_m;
        double dx_m;
        double dz_p;
        double dx_p;
        double Fi_m;
        double Fi_p;
        double J_m;
        double J_p;
        double p_curr;
        double v_curr;
        double p_neg;
        double p_pos;
        double d_t=new Long(this.getPeriod()).doubleValue()/this.getCntSegments();
        double Wa;
        double Wb;
        //Обновим значения предыдущего цикла
        this.setPOldNeg(numPoint,this.getPressCurrNegValue(numPoint));
        this.setPOldPos(numPoint,this.getPressCurrPosValue(numPoint));

        this.setVOldNeg(numPoint,this.getVelCurrNegValue(numPoint));
        this.setVOldPos(numPoint,this.getVelCurrNegValue(numPoint));
        // Тест:



        dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
        dz_p=0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
        dx_m =this.getSegmentLen();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        dx_p =this.getSegmentLen();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента


        Fi_p = (this.getLambda() * this.getDensity() * this.getVelOldPosValue(numPoint - 1)*Math.abs(this.getVelOldPosValue(numPoint - 1)));
        Fi_p=Fi_p/(2*this.getDiam()+this.getDensity()*this.g*(dz_p/dx_p));

        Fi_m = (this.getLambda() * this.getDensity() * this.getVelOldNegValue(numPoint + 1)*Math.abs(this.getVelOldNegValue(numPoint +1)));
        Fi_m=Fi_m/(2*this.getDiam()+this.getDensity()*this.g*(dz_m/dx_m));

        J_p = (this.getPressOldPosValue(numPoint -1) + (this.getDensity() * this.getSpeedWave() * this.getVelOldPosValue(numPoint - 1))) - dx_p * Fi_p;
        J_m = (this.getPressOldNegValue(numPoint +1) - (this.getDensity() * this.getSpeedWave() * this.getVelOldNegValue(numPoint + 1))) + dx_m * Fi_m;
        Wa=Math.pow((this.speed.doubleValue()/this.max_speed.doubleValue()),2)*approx_a;
        Wb=(this.speed.doubleValue()/this.max_speed.doubleValue())*approx_b;

        //скорость (положительная и отрицательная состовляющие равны)
        v_curr=(J_p-J_m+this.getDensity()*this.g*Wa)/(this.getDensity()*this.getSpeedWave());
        v_curr=v_curr*(1/(1+Math.sqrt(1+(Wb*this.getDensity()*this.g*Math.pow(((3600*Math.PI*this.getDiam()*this.getDiam())/4),2))))/(Math.pow(this.getDensity(),2)*Math.pow(this.getSpeedWave(),2)*(J_p-J_m+this.getDensity()*this.g*Wa)));

        this.setVCurrNeg(numPoint,v_curr);
        this.setVCurrPos(numPoint,v_curr);

        //давление (положительное)
        p_pos = J_m+this.getDensity()*this.getSpeedWave()*v_curr;// вычисляем текущее давление (положительное)
        this.setPCurrPos(numPoint,p_pos);
        //давление (отрицательное)
        p_neg = J_p-this.getDensity()*this.getSpeedWave()*v_curr;// вычисляем текущее давление (отрицательное)
        //Обновим значения текущего цикла
        this.setPCurrNeg(numPoint,p_neg);

        p_curr=p_pos;
        this.setPressure(numPoint-1,p_curr/1000000);
        this.setVelocity(numPoint-1,v_curr);
        //   System.out.println("Pipe: Point " + (numPoint) + " Current Pressure: " + (this.GetPressCurr_p() / 1000000) + " Current Velocity " + this.GetVelCurr_p());

    }


    @Override
    public void CalсPressure() {
        // перекладка данных для рассчета от левого объекта
        if (left_object!=null) {
            setPOldNeg(0, left_object.getOutPressure_neg());
            setPOldPos(0, left_object.getOutPressure_pos());
            setVOldNeg(0, left_object.getOutVelocity_neg());
            setVOldPos(0,  left_object.getOutVelocity_pos());
        }
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
    public ArrayList<Double> getCurrVelocity() {
        return this.getVelocity();
    }

    @Override
    public ArrayList<Double> getCurrPressure() {
        return this.getPressure();
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
    public int getNumBranch() {
        return this.getNumberBranch();
    }

    @Override
    public void setRightObject(IConnectedPipeObject rO) {
        right_object=rO;
    }

    @Override
    public void setLeftObject(IConnectedPipeObject lO) {
        left_object=lO;
    }
}
