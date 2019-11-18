package com.oilpipelinemodel.app;

import java.util.ArrayList;

// обратный клапан
class nRetrunValve extends aPipeObject implements ISheduled {
    IConnectedPipeObject left_object=null;
    IConnectedPipeObject right_object=null;

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
        double p_curr_pos;
        double p_curr_neg;
        double v_curr;
        double d_t=new Long(this.getPeriod()).doubleValue()/this.getCntSegments();
        //Обновим значения предыдущего цикла
        this.setPOldNeg(numPoint,this.getPressCurrNegValue(numPoint));
        this.setPOldPos(numPoint,this.getPressCurrPosValue(numPoint));

        this.setVOldNeg(numPoint,this.getVelCurrNegValue(numPoint));
        this.setVOldPos(numPoint,this.getVelCurrNegValue(numPoint));
        // Тест:



        dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
        dz_p=0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
        dx_m =(d_t/1000)*this.getSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        dx_p =(d_t/1000)*this.getSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента


        Fi_p = (this.getLambda() * this.getDensity() * this.getVelOldPosValue(numPoint - 1)*Math.abs(this.getVelOldPosValue(numPoint - 1)));
        Fi_p=Fi_p/(2*this.getDiam()+this.getDensity()*this.g*(dz_p/dx_p));

        Fi_m = (this.getLambda() * this.getDensity() * this.getVelOldNegValue(numPoint + 1)*Math.abs(this.getVelOldNegValue(numPoint +1)));
        Fi_m=Fi_m/(2*this.getDiam()+this.getDensity()*this.g*(dz_m/dx_m));

        J_p = (this.getPressOldPosValue(numPoint -1) + (this.getDensity() * this.getSpeedWave() * this.getVelOldPosValue(numPoint - 1))) - dx_p * Fi_p;
        J_m = (this.getPressOldNegValue(numPoint +1) - (this.getDensity() * this.getSpeedWave() * this.getVelOldNegValue(numPoint + 1))) + dx_m * Fi_m;

        if (J_p>J_m)
        {
            p_curr_pos = (J_p+J_m)/2;// вычисляем текущее давление
            p_curr_neg = (J_p+J_m)/2;// вычисляем текущее давление
        } else
            {
                p_curr_pos=J_m;
                p_curr_neg=J_p;
                J_p=J_m;
            }

        p_curr=p_curr_pos;

        //давление

        // на трубе нет разрыва, положительная и отрицательная составляющие равны (разрыв может быть на задвижке, регуляторе и насосе)

        //Обновим значения текущего цикла
        this.setPCurrNeg(numPoint,p_curr_neg);
        this.setPCurrPos(numPoint,p_curr_pos);
        // скорость
        v_curr = (J_p-J_m)/(2*this.getDensity()*this.getSpeedWave());

        //Обновим значения текущего цикла
        this.setVCurrNeg(numPoint,v_curr);
        this.setVCurrPos(numPoint,v_curr);

        this.setPressure(numPoint-1,p_curr/1000000);
        this.setVelocity(numPoint-1,v_curr);

      //  System.out.println("Non-return Valve: Point " + (numPoint) + " Current Pressure: " + (this.GetPressCurr_p() / 1000000) + " Current Velocity " + v_curr);
    }

    @Override
    public void CalсPressure()
    {
        // перекладка данных для рассчета от левого объекта
        if (left_object!=null) {
            setPOldNeg(0, left_object.getOutPressure_neg());
            setPOldPos(0, left_object.getOutPressure_pos());
            setVOldNeg(0, left_object.getOutVelocity_neg());
            setVOldPos(0, left_object.getOutVelocity_pos());
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

        // начало рассчета  по точкам
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
        return null;
    }

    @Override
    public ArrayList<Double> getCurrPressure() {
        return null;
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
}
