package com.oilpipelinemodel.app.Primitives;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedPoint;
import com.oilpipelinemodel.app.Interfaces.Primitives.ConnectedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ExchangedObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class EndTank extends BasePipeObject implements ConnectedObject, CalculatedObject, ExchangedObject {

    private double HeightTank;
    @Nullable
    private ExchangedObject leftObject = null;
    @Nullable
    private ExchangedObject rightObject = null;
    private final PipePoint pipePoint=new PipePoint();

    public EndTank( double heightTank, double Diameter, long length) {
        this.HeightTank = heightTank;
        this.setDiameter(Diameter);
        this.setLength(length);
    }

    void setHeightTank(double heightTank) {
        HeightTank = heightTank;
    }

    private CalculatedPoint getLeftPoint()
    {
        return (leftObject!=null)?leftObject.getLastPoint():null;
    }

    @Override
    public synchronized void calcParameters(){
        pipePoint.calc(this::refreshPointsValue); // запускаем расчет по одному сечению
    }

    public double[] refreshPointsValue() {

        double[] returnedValue=new double[DataOrder.getMaxIndex()];
        double dz_p = 0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
        double dx_p = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double Fi_p;
        double J_p;
        double currentPressure;
        double currentVelocity;

        CalculatedPoint leftPoint=getLeftPoint(); // только правая точка, левой нет
        if (leftPoint==null){ // нет левой или правой точки, вернем нулевой массив,т.к. физически это верно
            return new double[DataOrder.getMaxIndex()];
        }


        Fi_p = (this.getLambda() * this.getDensity() * leftPoint.getPrevPositiveVelocity() * Math.abs(leftPoint.getPrevPositiveVelocity()));
        Fi_p = Fi_p / (2 * this.getDiameter() + this.getDensity() * this.g * (dz_p / dx_p));

        J_p = (leftPoint.getPrevPositivePressure() + (this.getDensity() * this.getSpeedWave() * leftPoint.getPrevPositiveVelocity())) - dx_p * Fi_p;

        currentPressure = this.getDensity() * this.g * (this.HeightTank /*- this.getZValue(numPoint)*/); // вычисляем текущее давление
        // на резервуаре нет разрыва, положительная и отрицательная составляющие равны

        currentVelocity = (J_p - currentPressure) / (this.getDensity() * this.getSpeedWave());

        // на резервуаре нет разрыва, положительная и отрицательная составляющие равны
        // Обновим текущего значения
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
    public CalculatedPoint getFirstPoint() {
        return PipePoint.newCalculatedPointInstance(pipePoint);
    }

    @Override
    public CalculatedPoint getLastPoint() {
        return PipePoint.newCalculatedPointInstance(pipePoint);
    }

    @Override
    public String toString() {
        return "EndTank{" +
                pipePoint.toString() +
                '}';
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
    public ExchangedObject getSafeCopy() {
        EndTank returnedTank=new EndTank(HeightTank, this.getDiameter(), this.getLength());
        returnedTank.leftObject=leftObject;
        returnedTank.rightObject=rightObject;
        return returnedTank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndTank endTank = (EndTank) o;
        return Double.compare(endTank.HeightTank, HeightTank) == 0 &&
                Objects.equals(leftObject, endTank.leftObject) &&
                Objects.equals(rightObject, endTank.rightObject) &&
                Double.compare(this.getDiameter(), endTank.getDiameter())==0 &&
                Double.compare(this.getLength(), endTank.getLength())==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(HeightTank, leftObject, rightObject, this.getDiameter(), this.getLength());
    }
}