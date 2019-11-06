package com.oilpipelinemodel.app;
/*
* Класс трубопровода
* */


import java.util.ArrayList;

class PipeLine extends aPipeObject implements ISheduled {

    private IConnectedPipeObject left_object=null;
    private IConnectedPipeObject right_object=null;


    // рассчет в каждой точке
    private void CalcPressPoint(int numPoint)
    {
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
        double d_t=new Long(this.GetPeriod()).doubleValue()/this.getCntSegments();
        //Обновим значения предыдущего цикла
        this.SetP_old_neg(numPoint,this.GetPress_curr_negValue(numPoint));
        this.SetP_old_pos(numPoint,this.GetPress_curr_posValue(numPoint));

        this.SetV_old_neg(numPoint,this.GetVel_curr_negValue(numPoint));
        this.SetV_old_pos(numPoint,this.GetVel_curr_negValue(numPoint));

            dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
            dz_p=0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
            dx_m =this.GetSegmentLen();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
            dx_p =this.GetSegmentLen();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента


            Fi_p = (this.GetLambda() * this.GetDensity() * this.GetVel_old_posValue(numPoint - 1)*Math.abs(this.GetVel_old_posValue(numPoint - 1)));
            Fi_p=Fi_p/(2*this.GetDiam()+this.GetDensity()*this.g*(dz_p/dx_p));

            Fi_m = (this.GetLambda() * this.GetDensity() * this.GetVel_old_negValue(numPoint + 1)*Math.abs(this.GetVel_old_negValue(numPoint +1)));
            Fi_m=Fi_m/(2*this.GetDiam()+this.GetDensity()*this.g*(dz_m/dx_m));

            J_p = (this.GetPress_old_posValue(numPoint -1) + (this.GetDensity() * this.GetSpeedWave() * this.GetVel_old_posValue(numPoint - 1))) - dx_p * Fi_p;
            J_m = (this.GetPress_old_negValue(numPoint +1) - (this.GetDensity() * this.GetSpeedWave() * this.GetVel_old_negValue(numPoint + 1))) + dx_m * Fi_m;


            //давление
            p_curr = (J_p+J_m)/2;// вычисляем текущее давление
            // на трубе нет разрыва, положительная и отрицательная составляющие равны (разрыв может быть на задвижке, регуляторе и насосе)
        //Обновим значения текущего цикла
             this.SetP_curr_neg(numPoint,p_curr);
             this.SetP_curr_pos(numPoint,p_curr);
            // скорость
            v_curr = (J_p-J_m)/(2*this.GetDensity()*this.GetSpeedWave());

        //Обновим значения текущего цикла
             this.SetV_curr_neg(numPoint,v_curr);
             this.SetV_curr_pos(numPoint,v_curr);

            this.setPressure(numPoint-1,p_curr/1000000);
            this.setVelocity(numPoint-1,v_curr);
       //   System.out.println("Pipe: Point " + (numPoint) + " Current Pressure: " + (this.GetPressCurr_p() / 1000000) + " Current Velocity " + this.GetVelCurr_p());

    }

    @Override
    public void CalсPressure()
    {
        // перекладка данных для рассчета от левого объекта
        if (left_object!=null) {
            SetP_old_neg(0, left_object.getOutPressure_neg());
            SetP_old_pos(0, left_object.getOutPressure_pos());
            SetV_old_neg(0, left_object.getOutVelocity_neg());
            SetV_old_pos(0,  left_object.getOutVelocity_pos());
        }
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
        return this.getVelocity();
    }

    @Override
    public ArrayList<Double> getCurrPressure() {
        return this.getPressure();
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
