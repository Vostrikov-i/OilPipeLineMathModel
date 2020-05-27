package com.oilpipelinemodel.app.Primitives;

/*Насос
*
*
*
* */


import com.oilpipelinemodel.app.Interfaces.Primitives.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Pump extends BasePipeObject implements ConnectedObject, CalculatedObject, ExchangedObject, ManagedObject<Double> {

    private double speed; // скорость вращения двигателя
    private double ACoefficientApprox=305.0; // коэффициент апроксимации функции напора от расхода
    private double BCoefficientApprox=0.00000208; // коэффициент апроксимации функции напора от расхода
    private double maxSpeed; // максимальная скорость вращения двигателя насоса
    private final PipePoint pipePoint=new PipePoint();
    @Nullable
    private ExchangedObject leftObject =null;
    @Nullable
    private ExchangedObject rightObject =null;

    public Pump( double diameter, long length, double maxSpeed){
        this.setDiameter(diameter);
        this.setLength(length);
        this.maxSpeed=maxSpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getACoefficientApprox() {
        return ACoefficientApprox;
    }

    public void setACoefficientApprox(double ACoefficientApprox) {
        this.ACoefficientApprox = ACoefficientApprox;
    }

    public double getBCoefficientApprox() {
        return BCoefficientApprox;
    }

    public void setBCoefficientApprox(double BCoefficientApprox) {
        this.BCoefficientApprox = BCoefficientApprox;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }


    private CalculatedPoint getLeftPoint()
    {
        return (leftObject!=null)?leftObject.getLastPoint():null;
    }

    private CalculatedPoint getRightPoint()
    {
        return (rightObject!=null)?rightObject.getFirstPoint():null;
    }

    @Override
    public synchronized void calcParameters()  {
        pipePoint.calc(this::refreshPointsValue); // запускаем расчет по одному сечению
    }

    private double[] refreshPointsValue() {
        double[] returnedValue=new double[DataOrder.getMaxIndex()];
        double dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
        double dz_p = 0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
        double dx_m = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double dx_p = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double Fi_m;
        double Fi_p;
        double J_m;
        double J_p;
        double currentPressure;
        double currentVelocity;
        double Wa;
        double Wb;
        double positivePressure;
        double negativePressure;

        CalculatedPoint leftPoint= getLeftPoint();
        CalculatedPoint rightPoint=getRightPoint();
        if (leftPoint==null || rightPoint==null){ // нет левой или правой точки, вернем нулевой массив,т.к. физически это верно
            return new double[DataOrder.getMaxIndex()];
        }

        Fi_p = (this.getLambda() * this.getDensity() * leftPoint.getPrevPositiveVelocity()*Math.abs(leftPoint.getPrevPositiveVelocity()));
        Fi_p=Fi_p/(2*this.getDiameter()+this.getDensity()*this.g*(dz_p/dx_p));

        Fi_m = (this.getLambda() * this.getDensity() * rightPoint.getPrevNegativeVelocity()*Math.abs(rightPoint.getPrevNegativeVelocity()));
        Fi_m=Fi_m/(2*this.getDiameter()+this.getDensity()*this.g*(dz_m/dx_m));

        J_p = (leftPoint.getPrevPositivePressure() + (this.getDensity() * this.getSpeedWave() * leftPoint.getPrevPositiveVelocity())) - dx_p * Fi_p;
        J_m = (rightPoint.getPrevNegativePressure() - (this.getDensity() * this.getSpeedWave() * rightPoint.getPrevNegativeVelocity())) + dx_m * Fi_m;
        Wa=Math.pow((speed/maxSpeed),2)*ACoefficientApprox;
        Wb=(speed/maxSpeed)*BCoefficientApprox;

        //скорость (положительная и отрицательная составляющие равны)
        currentVelocity=(J_p-J_m+this.getDensity()*this.g*Wa)/(this.getDensity()*this.getSpeedWave());
        currentVelocity=currentVelocity*(1/(1+Math.sqrt(1+(Wb*this.getDensity()*this.g*Math.pow(((3600*Math.PI*this.getDiameter()*this.getDiameter())/4),2))))/(Math.pow(this.getDensity(),2)*Math.pow(this.getSpeedWave(),2)*(J_p-J_m+this.getDensity()*this.g*Wa)));

        //давление (положительное)
        positivePressure = J_m+this.getDensity()*this.getSpeedWave()*currentVelocity;// вычисляем текущее давление (положительное)
        //давление (отрицательное)
        negativePressure = J_p-this.getDensity()*this.getSpeedWave()*currentVelocity;// вычисляем текущее давление (отрицательное)

        currentPressure=positivePressure;

        // + и - составляющие давлений отличаются, скорости равны
        returnedValue[DataOrder.PRESSURE.getIndex()]=currentPressure/1000000; //в МПа
        returnedValue[DataOrder.VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.POSITIVE_PRESSURE.getIndex()]=positivePressure;
        returnedValue[DataOrder.NEGATIVE_PRESSURE.getIndex()]=negativePressure;
        returnedValue[DataOrder.POSITIVE_VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.NEGATIVE_VELOCITY.getIndex()]=currentVelocity;

        return returnedValue;
    }

    @Override
    public CalculatedPoint getFirstPoint() {
        return PipePoint.newCalculatedPointInstance(pipePoint);
    }

    @Override
    public CalculatedPoint getLastPoint() {
        return PipePoint.newCalculatedPointInstance(pipePoint);
    }

    @Override
    public void setLeftObject(ExchangedObject exchangedObject) {
        leftObject =exchangedObject;
    }

    @Override
    public void setRightObject(ExchangedObject exchangedObject) {
        rightObject =exchangedObject;
    }


    @Override
    public void receiveCommand(Double value) {
        this.setSpeed(value);
    }

    @Override
    public ExchangedObject getSafeCopy() {
        Pump returnedPump=new Pump(this.getDiameter(), this.getLength(), maxSpeed);
        returnedPump.leftObject=leftObject;
        returnedPump.rightObject=rightObject;
        return returnedPump;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pump pump = (Pump) o;
        return Double.compare(pump.ACoefficientApprox, ACoefficientApprox) == 0 &&
                Double.compare(pump.BCoefficientApprox, BCoefficientApprox) == 0 &&
                Double.compare(pump.maxSpeed, maxSpeed) == 0 &&
                Objects.equals(leftObject, pump.leftObject) &&
                Objects.equals(rightObject, pump.rightObject) &&
                Double.compare(this.getDiameter(), pump.getDiameter())==0 &&
                Double.compare(this.getLength(), pump.getLength())==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ACoefficientApprox, BCoefficientApprox, maxSpeed, leftObject, rightObject, this.getDiameter(), this.getLength());
    }
}
