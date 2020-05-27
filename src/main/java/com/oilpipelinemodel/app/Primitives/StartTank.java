package com.oilpipelinemodel.app.Primitives;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedPoint;
import com.oilpipelinemodel.app.Interfaces.Primitives.ConnectedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ExchangedObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/*
* Класс стартовой емкости. Нефтепровод начинается с нее, слева ничего не подключается
* У емкости одна точка сечения, большее количество точек сечения лишено смысла
*
*
*
* */

public final class StartTank extends BasePipeObject implements ConnectedObject, CalculatedObject, ExchangedObject {

    private double HeightTank;
    private final PipePoint pipePoint=new PipePoint();
    @Nullable
    private ExchangedObject leftObject = null;
    @Nullable
    private ExchangedObject rightObject = null;

    public StartTank(double heightTank, double Diameter, long length) {
        this.HeightTank = heightTank;
        this.setDiameter(Diameter);
        this.setLength(length);
    }

    void setHeightTank(double heightTank) {
        HeightTank = heightTank;
    }

    private CalculatedPoint getRightPoint()
    {
        return (rightObject!=null)?rightObject.getFirstPoint():null;
    }

    @Override
    public synchronized void calcParameters(){
        pipePoint.calc(this::refreshPointsValue); // запускаем расчет по одному сечению
    }

    private double[] refreshPointsValue() {
        double[] returnedValue=new double[DataOrder.getMaxIndex()];
        double dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
        double dx_m = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double Fi_m;
        double J_m;
        double currentPressure;
        double currentVelocity;

        CalculatedPoint rightPoint=getRightPoint(); // только правая точка, левой нет
        if (rightPoint==null){ // нет левой или правой точки, вернем нулевой массив,т.к. физически это верно
            return new double[DataOrder.getMaxIndex()];
        }

        Fi_m = (this.getLambda() * this.getDensity() * rightPoint.getPrevNegativeVelocity() * Math.abs(rightPoint.getPrevNegativeVelocity()));
        Fi_m = Fi_m / (2 * this.getDiameter() + this.getDensity() * this.g * (dz_m / dx_m)); // просто потому что не влазило в одну строку
        J_m = (rightPoint.getPrevNegativePressure() - (this.getDensity() * this.getSpeedWave() * (rightPoint.getPrevNegativeVelocity()))) + dx_m * Fi_m;

        currentPressure = this.getDensity() * this.g * (this.HeightTank/* - this.getZValue(numPoint)*/); // вычисляем текущее давление
        currentVelocity = (currentPressure - J_m) / (this.getDensity() * this.getSpeedWave());


        returnedValue[DataOrder.PRESSURE.getIndex()]=currentPressure/1000000; //в МПа;
        returnedValue[DataOrder.VELOCITY.getIndex()]=currentVelocity;
        // + и - составляющие равны
        returnedValue[DataOrder.POSITIVE_PRESSURE.getIndex()]=currentPressure;
        returnedValue[DataOrder.NEGATIVE_PRESSURE.getIndex()]=currentPressure;
        returnedValue[DataOrder.POSITIVE_VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.NEGATIVE_VELOCITY.getIndex()]=currentVelocity;

        return returnedValue;
    }

    @Override
    public String toString() {
        return "StartTank{" +"\n"+
                 pipePoint.toString() +"\n"+
                '}';
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
    public ExchangedObject getSafeCopy() {
        StartTank returnedTank=new StartTank(HeightTank, this.getDiameter(), this.getLength());
        returnedTank.leftObject=leftObject;
        returnedTank.rightObject=rightObject;
        return returnedTank;
    }

    @Override
    public void setLeftObject(ExchangedObject exchangedObject) {
        leftObject=exchangedObject;
    }

    @Override
    public void setRightObject(ExchangedObject exchangedObject) {
        rightObject=exchangedObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartTank startTank = (StartTank) o;
        return Double.compare(startTank.HeightTank, HeightTank) == 0 &&
                Objects.equals(leftObject, startTank.leftObject) &&
                Objects.equals(rightObject, startTank.rightObject) &&
                Double.compare(this.getDiameter(), startTank.getDiameter())==0 &&
                Double.compare(this.getLength(), startTank.getLength())==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(HeightTank, leftObject, rightObject, this.getDiameter(), this.getLength());
    }
}

