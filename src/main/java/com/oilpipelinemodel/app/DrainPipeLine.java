package com.oilpipelinemodel.app;

import java.util.ArrayList;

/*
* Трубопровод с разветвлением
* Считаем данный объект "виртуальным"
* Т.е он существует в карте объектов нефтепровода
* У него есть методы для запуска рассчета, однако из него нельзя получить давления и скорости
* В нем создаются два объекта типа PipeLine, т.е. обычный трубопровод,
* туда по окончанию рассчетов копируются данные о давлениях и скоростях и с ними уже соединяются соседние объекты
*
* */
class DrainPipeLine extends aPipeObject implements ISheduled, IBranched {

        private IBranched top_object;
        private IBranched down_object;
        private IConnectedPipeObject left_object=null;
        private IConnectedPipeObject right_object=null;

    /*
    * Замороженные значения нужны для корректного рассчета ветвления
    * Т.к ветвление занимает не одну нитку, до для рассчета давлений по текущей нитке нужны давления на прошлом шаге в следующей нитке
    * Однако при
    *
    *
    *
    * */
        private ArrayList<Double> freezePressure_pos; //замороженное значение положительной составляющей давления на прошлом шаге
        private ArrayList<Double> freezePressure_neg; //замороженное значение отрциательной составляющей давления на прошлом шаге
        private ArrayList<Double> freezeVelocity_pos; //замороженное значение положительной составляющей скорости на прошлом шаге
        private ArrayList<Double> freezeVelocity_neg; //замороженное значение отрциательной составляющей скорости на прошлом шаге

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
    public IBranched getTopObject() {
        return this.top_object;
    }

    @Override
    public IBranched getDownObject() {
        return this.down_object;
    }

    @Override
    public void setTopObject(IBranched tO) {
        this.top_object=tO;
    }

    @Override
    public void setDownObject(IBranched dO) {
        this.down_object=dO;
    }

    public void updateFreezeData() {
        freezePressure_pos=this.GetPress_old_pos();
        freezePressure_neg=this.GetPress_old_neg();
        freezeVelocity_pos=this.GetVel_old_pos();
        freezeVelocity_neg=this.GetVel_old_neg();
    }

    @Override
    public void setLeftObject(IConnectedPipeObject lO) {
        left_object=lO;
    }

    @Override
    public void CalсPressure() {
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

        if (top_object!=null || down_object!=null) { // рассчет ветвления запустится только если оно полностью собрано (снизу или сверху присоединен объект)
            // начало рассчета  по точкам
            for (int i = 1; (i < this.getCntSegments() + 1); i++) {
                CalcPressPoint(i);
            }
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


    private void CalcPressPoint(int numPoint) {

        boolean isEndBranch; // признак последней нитки в ветвлении (для него особый алгоритм рассчета)
        double dz_m;
        double dx_m;
        double dz_p;
        double dx_p;
        double Fi_m;
        double Fi_p;
        double J_m;
        double J_p;
        double J_p_down;
        double J_m_down;
        ArrayList<Double> Press_neigBranch_pos; //давление с соседней нитки положительное
        ArrayList<Double> Press_neigBranch_neg; //давление с соседней нитки отрицательное
        ArrayList<Double> Vel_neigBranch_pos; //скорость с соседней нитки положительная
        ArrayList<Double> Vel_neigBranch_neg; // скорость с соседней нитки отрицательная
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


    }



}
