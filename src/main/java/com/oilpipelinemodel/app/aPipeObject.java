package com.oilpipelinemodel.app;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

abstract class aPipeObject
{

    //Служебные переменные
    private static AtomicLong nextId = new AtomicLong(0); // для корректной работы с многопоточностью
    private long id=nextId.incrementAndGet(); // уникальный id объекта

    private long Cnt_Segments; // количество сегментов рассчета, на которые мы разбили участок
    private int numBranch=0; // номер нитки трубопровода
    private long  period=100; // период рассчета

    //Абстрактные (не как классы) переменные для рассчета
    private double Diam=0.2; // диаметр трубы на участке, для резервуара диаметр выходящей трубы
    private long segment_len=10; // шаг с которым делается сечение трубопровода
    private long PipeLen=10; // длина участка в метрах


    private ArrayList<Double> Z = new ArrayList<Double>(); // Высоты по каждому сечению

    private ArrayList<Double> Press_old_pos = new ArrayList<Double>(); // Массивы значений давления на прошлом шаге по всем точкам сечений (положительная)
    private ArrayList<Double> Press_old_neg = new ArrayList<Double>();  // Массивы значений давления на прошлом шаге по всем точкам сечений  (Отрицательная)
    private ArrayList<Double> Vel_old_pos = new ArrayList<Double>(); // Массивы значений скорости на прошлом шаге по всем точкам сечений (положительная)
    private ArrayList<Double> Vel_old_neg = new ArrayList<Double>(); // Массивы значений скорости на прошлом шаге по всем точкам сечений (Отрицательная)

    private ArrayList<Double> Press_curr_pos = new ArrayList<Double>(); // Массивы значений давления на текущем шаге по всем точкам сечений (положительная)
    private ArrayList<Double> Press_curr_neg = new ArrayList<Double>(); // Массивы значений давления на текущем шаге по всем точкам сечений  (Отрицательная)
    private ArrayList<Double> Vel_curr_pos = new ArrayList<Double>(); // Массивы значений скорости на текущем шаге по всем точкам сечений (положительная)
    private ArrayList<Double> Vel_curr_neg = new ArrayList<Double>(); // Массивы значений скорости на текущем шаге по всем точкам сечений (Отрицательная)


    private ArrayList<Double> Pressure = new ArrayList<Double>(); // Массив давлений
    private ArrayList<Double> Velocity = new ArrayList<Double>(); // Массив скоростей

    ///Коэффициенты трубопровода
    private double Lamda; // Коэффициент гидравлического сопротивления, вычисляем по формуле Альтшуля
    private double epsilon; // коэффициент относительной шероховатости труб
    private double ReinNum; // число Рейнольдса, формуля Альтшуля справедлива для числа Рейнольдса от 10^4 до 10^6
    private double ReinVel; // скорость потока для рассчета числа Рейнольдсаp


    //Формула для вычисления числа Рейнольдса (скорость*диаметр)/(кинематическую вязкость)
    //скорость высчитывается из расхода по формуле Vel=(4*Q)/2*pi*d^2

    //Статичные переменные
    private static double speedWave=1225; // скорость распространения волн в трубопроводе (по сути скорость звука в среде перекачки) полагаем везде одинаковой
    private static double Density=960; // плотность перекачиваемой жидкости, полагаем везде одинаковой

    private static double StartQ=10000; // стартовый расход в нефтепроводе м^3/ч
    private static int MAX_NUM_BRANCH=0; // максимальное количество ниток трубопроводов (0 - значит трубопровод в 1 нитку, изменяется динамически, при доавлении новой нитки каким либо объектом PipeObject)

    //TODO в дальнейшем сделать коэффициенты не константами
    //Константы
    final double visc=300; // Вязкость, предположим для простоты среднее значние 300 мм2/с
    final double g=9.81; //ускорение свободного падения
    final double K=0.15; // Эквивалентная шероховатость труб, для рассчета коэффициента относительной шероховатости








    //******************** ИНИЦИАЛИЗАЦИЯ *************************

    aPipeObject(){
        resizeCalcArr(1); // инициализируем массивы и таймера
    }


    //----------------------------------------------------

    protected long getPeriod(){return this.period;}
    protected long getId(){return this.id;} // Снаружи не даем доступ на изменение
    protected long getSegmentLen() {return this.segment_len;}
    protected double getSpeedWave(){return speedWave;}
    protected double getDensity(){return Density;}
    protected double getLambda()
    {
        double pow;
        if (this.Diam>0 && this.visc>0) {
            this.epsilon = (K * Math.pow(10, -3)) / this.Diam; // относительная шероховатость (10^-3 для перевода из мм в м)
            this.ReinVel = (4 * this.StartQ) / (3600 * Math.PI * Math.pow(this.Diam, 2)); // пересчитываем скорость из расхода (расход в м3/ч, скорость в м/с)
            this.ReinNum = (this.ReinVel * this.Diam) / (this.visc * Math.pow(10, -6)); // 10^-6 для перевода из мм2/с в м2/с
        }
        if (this.ReinNum!=0) {
            pow=Math.pow((this.epsilon + (68 / (this.ReinNum))), 0.25);
            this.Lamda = 0.11 * pow; //
        }
        return this.Lamda;

    }
    protected double getDiam(){return this.Diam;}
    protected double getEpsilon(){return this.epsilon;}
    protected double getReinNum(){return this.ReinNum;}
    protected long getPipeLen(){return this.PipeLen;}
    protected int getNumberBranch(){return this.numBranch;}

    // Getter для всего массива
    protected ArrayList<Double> getZ() {return this.Z;}
    protected ArrayList<Double> getPressOldPos() {return this.Press_old_pos;}
    protected ArrayList<Double> getPressOldNeg() {return this.Press_old_neg;}
    protected ArrayList<Double> getVelOldNeg() {return this.Vel_old_neg;}
    protected ArrayList<Double> getVelOldPos() {return this.Vel_old_pos;}


    //TODO добавить обработку исключений по массиву
    protected Double getZValue(int index)
    {
        if (index>=0 & index<(this.Z.size()))
        {
            return this.Z.get(index);
        } else
            {
                return null;
            }
    }
    protected Double getPressOldPosValue(int index)
    {
        if (index>=0 & index<(this.Press_old_pos.size()))
        {
            return this.Press_old_pos.get(index);
        } else
        {
            return null;
        }
    }
    protected Double getPressOldNegValue(int index)
    {
        if (index>=0 & index<(this.Press_old_neg.size()))
        {
            return this.Press_old_neg.get(index);
        } else
        {
            return null;
        }
    }
    protected Double getVelOldNegValue(int index)
    {
        if (index>=0 & index<(this.Vel_old_neg.size()))
        {
            return this.Vel_old_neg.get(index);
        } else
        {
            return null;
        }
    }
    protected Double getVelOldPosValue(int index)
    {
        if (index>=0 & index<(this.Vel_old_pos.size()))
        {
            return this.Vel_old_pos.get(index);
        } else
        {
            return null;
        }

    }

    protected Double getPressCurrPosValue(int index)
    {
        if (index>=0 & index<(this.Press_curr_pos.size()))
        {
            return this.Press_curr_pos.get(index);
        } else
        {
            return null;
        }
    }
    protected Double getPressCurrNegValue(int index)
    {
        if (index>=0 & index<(this.Press_curr_neg.size()))
        {
            return this.Press_curr_neg.get(index);
        } else
        {
            return null;
        }
    }
    protected Double getVelCurrNegValue(int index)
    {
        if (index>=0 & index<(this.Vel_curr_neg.size()))
        {
            return this.Vel_curr_neg.get(index);
        } else
        {
            return null;
        }
    }
    protected Double getVelCurrPosValue(int index)
    {
        if (index>=0 & index<(this.Vel_curr_pos.size()))
        {
            return this.Vel_curr_pos.get(index);
        } else
        {
            return null;
        }

    }
    protected ArrayList<Double> getPressure()
    {
        return this.Pressure;
    }

    protected ArrayList<Double> getVelocity()
    {
        return this.Velocity;
    }

    protected double getStartQ() {return StartQ;}
    protected long getCntSegments(){return Cnt_Segments;}

    //---------------------------------------------------------------------------------------------------------------Setter

    protected boolean setNumBranch(int numBranch)
    {
        if(numBranch>=0)
        {
            this.numBranch=numBranch;
            if (MAX_NUM_BRANCH<this.numBranch) MAX_NUM_BRANCH=this.numBranch;
            return true;
        }
        else return false;
    }
    protected void setSegmentLen(long segment_len) // Длина шага разбиения участка
    {
        this.segment_len=segment_len;
        if (this.PipeLen>0 & this.segment_len>0)
        {
          //  this.end_segment=(this.PipeLen/this.segment_len); // конечный сегмент высчитываем на основании длины участка, начального сегмента и длины сегмента
            Cnt_Segments=(this.PipeLen/this.segment_len); // высчитываем количество сегментов
            resizeCalcArr(Cnt_Segments);
        }
    }

    protected void setSpeedWave(double s_wave){if (s_wave>0) speedWave=s_wave;} //Скорость распространения волн в трубопроводе
    protected void setDensity(double dens){if (dens>0) Density=dens;} // Плотность нефти в трубопроводе
    protected void setStartQ(double inStartQ) {StartQ=inStartQ;} //Стартовый расход



    protected void setPipeLen(long PipeLen)
    {
        if (PipeLen>0) this.PipeLen=PipeLen;
        if (this.PipeLen>0 & this.segment_len>0)
        {
            //  this.end_segment=(this.PipeLen/this.segment_len); // конечный сегмент высчитываем на основании длины участка, начального сегмента и длины сегмента
            Cnt_Segments=(this.PipeLen/this.segment_len); // высчитываем количество сегментов
            resizeCalcArr(Cnt_Segments);
        }
    }



    protected void setDiam(double Diam)
     {
      if (Diam>0)
       {
          this.Diam=Diam;
          //Т.к характеристики нефтепровода зависят от диаметра, то пересчитываем при каждом изменении диаметра
          this.epsilon=(K*Math.pow(10,-3))/this.Diam; // относительная шероховатость (10^-3 для перевода из мм в м)
           this.ReinVel=(4*this.StartQ)/(3600*Math.PI*Math.pow(this.Diam,2)); // пересчитываем скорость из расхода (расход в м3/ч, скорость в м/с)
          this.ReinNum=(this.ReinVel*this.Diam)/(this.visc*Math.pow(10,-6)); // 10^-6 для перевода из мм2/с в м2/с
          this.Lamda=0.11*Math.pow((this.epsilon+(68/(this.ReinNum))), 1/4); //
       }
    }
    protected void setZ(int index, Double Z) { this.Z.set(index, Z); }
    protected void setPOldPos(int index, Double P_old_pos) { this.Press_old_pos.set(index, P_old_pos); }
    protected void setPOldNeg(int index, Double P_old_neg) { this.Press_old_neg.set(index, P_old_neg); }
    protected void setVOldPos(int index, Double V_old_pos) { this.Vel_old_pos.set(index, V_old_pos); }
    protected void setVOldNeg(int index, Double V_old_neg) { this.Vel_old_neg.set(index, V_old_neg); }

    protected void setPCurrPos(int index, Double P_curr_pos) { this.Press_curr_pos.set(index, P_curr_pos); }
    protected void setPCurrNeg(int index, Double P_curr_neg) { this.Press_curr_neg.set(index, P_curr_neg); }
    protected void setVCurrPos(int index, Double V_curr_pos) { this.Vel_curr_pos.set(index, V_curr_pos); }
    protected void setVCurrNeg(int index, Double V_curr_neg) { this.Vel_curr_neg.set(index, V_curr_neg); }

    protected void setPressure(int point, Double value)
    {
        if (point>=0 && point<this.Pressure.size())
        {
             this.Pressure.set(point,value);
        }
    }

    protected void setVelocity(int point, Double value)
    {
        if (point>=0 && point<this.Velocity.size())
        {
            this.Velocity.set(point,value);
        }
    }

    private void resizeCalcArr(long cnt)
    {
        double tmp_period;
        /*
                 теперь выделим память под массивы Z координат, и массивы давлений и скоростей
                размер массивов всегда совпадает с количеством точек разбиения
                Т.к. изменение количества сегмнетов для рассчета приводит к необходимости пересчитывать всю модель с нуля
                то можно очистить ArrayList и создать его заново
             */
        Z.clear();
        Press_old_pos.clear();
        Press_old_neg.clear();
        Vel_old_neg.clear();
        Vel_old_pos.clear();
        Press_curr_pos.clear();
        Press_curr_neg.clear();
        Vel_curr_neg.clear();
        Vel_curr_pos.clear();
        Pressure.clear();
        Velocity.clear();
        // заполняем нулями массивы под наши размеры,
        for (int i=0;i<=cnt+1;i++) // на самом деле нам надо на 2 элемента больше, 0 элемент это точка подключения слева, CntSegment+1 - точка подключения справа
        {
            Z.add(0.0);
            Press_curr_pos.add(0.0);
            Press_curr_neg.add(0.0);
            Vel_curr_neg.add(0.0);
            Vel_curr_pos.add(0.0);
            Press_old_pos.add(0.0);
            Press_old_neg.add(0.0);
            Vel_old_neg.add(0.0);
            Vel_old_pos.add(0.0);
        }
        for (int i=0;i<cnt;i++) { // давления и скорости для передачи наружу заполняются по реальному количеству точек разбиения
            Pressure.add(0.0);
            Velocity.add(0.0);
        }
        tmp_period=1000*(this.segment_len/speedWave)*cnt;
        if (tmp_period<1) tmp_period=1;
        this.period=(new Double(tmp_period)).longValue(); // пересчитываем период рассчета

    }

}
