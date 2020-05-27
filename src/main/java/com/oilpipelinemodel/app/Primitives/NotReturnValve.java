package com.oilpipelinemodel.app.Primitives;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedPoint;
import com.oilpipelinemodel.app.Interfaces.Primitives.ConnectedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ExchangedObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

// обратный клапан
public final class NotReturnValve extends BasePipeObject implements ConnectedObject, CalculatedObject, ExchangedObject {

    @Nullable
    private ExchangedObject leftObject=null;
    @Nullable
    private ExchangedObject rightObject=null;

    private final PipePoint pipePoint=new PipePoint();

    public NotReturnValve(double Diameter, long length){
        this.setDiameter(Diameter);
        this.setLength(length);
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
    public synchronized void calcParameters(){
        pipePoint.calc(this::refreshPointsValue); // запускаем расчет по одному сечению
    }

    public double[] refreshPointsValue() {
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
        double currentPressurePositive;
        double currentPressureNegative;

        CalculatedPoint leftPoint= getLeftPoint();
        CalculatedPoint rightPoint=getRightPoint();
        if (leftPoint==null || rightPoint==null){ // нет левой или правой точки, вернем нулевой массив,т.к. физически это верно
            return new double[DataOrder.getMaxIndex()];
        }


        Fi_p = (this.getLambda() * this.getDensity() *  leftPoint.getPrevPositiveVelocity()*Math.abs(leftPoint.getPrevPositiveVelocity()));
        Fi_p=Fi_p/(2*this.getDiameter()+this.getDensity()*this.g*(dz_p/dx_p));

        Fi_m = (this.getLambda() * this.getDensity() * rightPoint.getPrevNegativeVelocity()*Math.abs(rightPoint.getPrevNegativeVelocity()));
        Fi_m=Fi_m/(2*this.getDiameter()+this.getDensity()*this.g*(dz_m/dx_m));

        J_p = ((leftPoint.getPrevPositivePressure() + (this.getDensity() * this.getSpeedWave() * leftPoint.getPrevPositiveVelocity()))) - dx_p * Fi_p;
        J_m = (rightPoint.getPrevNegativePressure() - (this.getDensity() * this.getSpeedWave() * rightPoint.getPrevNegativeVelocity())) + dx_m * Fi_m;



        currentPressurePositive=(J_p>J_m)?((J_p+J_m)/2):J_m;
        currentPressureNegative=(J_p>J_m)?((J_p+J_m)/2):J_p;
        J_p=(J_p>J_m)?J_p:J_m;

        currentPressure=currentPressurePositive; //берем положительную, но можно и отрицательную, главное везде брать одно и то де
        currentPressure=(J_p+J_m)/2;
        currentVelocity=(J_p-J_m)/(2*this.getDensity()*this.getSpeedWave());

        // + и - составляющие равны
        returnedValue[DataOrder.PRESSURE.getIndex()]=currentPressure/1000000; //в МПа
        returnedValue[DataOrder.VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.POSITIVE_PRESSURE.getIndex()]=currentPressurePositive;
        returnedValue[DataOrder.NEGATIVE_PRESSURE.getIndex()]=currentPressureNegative;
        returnedValue[DataOrder.POSITIVE_VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.NEGATIVE_VELOCITY.getIndex()]=currentVelocity;
        return returnedValue;
    }


    @Override
    public CalculatedPoint getFirstPoint() {
        return pipePoint;
    }

    @Override
    public CalculatedPoint getLastPoint() {
        return pipePoint;
    }

    @Override
    public ExchangedObject getSafeCopy() {
        NotReturnValve returnedNotReturnValve=new NotReturnValve(this.getDiameter(), this.getLength());
        returnedNotReturnValve.leftObject=leftObject;
        returnedNotReturnValve.rightObject=rightObject;
        return returnedNotReturnValve;
    }

    @Override
    public void setLeftObject(ExchangedObject exchangedObject) {
        leftObject =exchangedObject;
    }

    @Override
    public void setRightObject(ExchangedObject exchangedObject) {
        rightObject=exchangedObject;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotReturnValve notReturnValve = (NotReturnValve) o;
        return  Objects.equals(leftObject, notReturnValve.leftObject) &&
                Objects.equals(rightObject, notReturnValve.rightObject) &&
                Double.compare(this.getDiameter(), notReturnValve.getDiameter())==0 &&
                Double.compare(this.getLength(), notReturnValve.getLength())==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftObject, rightObject, this.getDiameter(), this.getLength());
    }
}
