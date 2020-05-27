package com.oilpipelinemodel.app.Primitives;

import com.oilpipelinemodel.app.Interfaces.Primitives.*;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/*заслонка
*
*
*
* */
public final class Damper extends BasePipeObject implements ConnectedObject, CalculatedObject, ManagedObject<Double>, ExchangedObject {

    private double flapPosition;
    private double CoefficientApprox = 2500.0; // коэффициент аппроксимации для рассчета коэффициента расхода
    @Nullable
    private ExchangedObject leftObject = null;
    @Nullable
    private ExchangedObject rightObject = null;
    private final PipePoint pipePoint=new PipePoint();


    public Damper(double diameter, long length, double CoefficientApprox){
        this.setDiameter(diameter);
        this.setLength(length);
        this.CoefficientApprox=CoefficientApprox;
    }

    double getFlapPosition() {
        return flapPosition;
    }

    void setFlapPosition(double flapPosition) {
        this.flapPosition = flapPosition;
    }

    double getCoefficientApprox() {
        return CoefficientApprox;
    }

    void setCoefficientApprox(double coefficientApprox) {
        CoefficientApprox = coefficientApprox;
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
        double positivePressure;
        double negativePressure;
        double Kv;
        double tmpValue1;
        double tmpValue2;


        dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
        dz_p = 0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
        dx_m = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        dx_p = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        CalculatedPoint leftPoint= getLeftPoint();
        CalculatedPoint rightPoint=getRightPoint();

        if (leftPoint==null || rightPoint==null){ // нет левой или правой точки, вернем нулевой массив,т.к. физически это верно
            return new double[DataOrder.getMaxIndex()];
        }

        Fi_p = (this.getLambda() * this.getDensity() * leftPoint.getPrevPositiveVelocity() * Math.abs(leftPoint.getPrevPositiveVelocity() ));
        Fi_p = Fi_p / (2 * this.getDiameter() + this.getDensity() * this.g * (dz_p / dx_p));

        Fi_m = (this.getLambda() * this.getDensity() * rightPoint.getPrevNegativeVelocity() * Math.abs(rightPoint.getPrevNegativeVelocity()));
        Fi_m = Fi_m / (2 * this.getDiameter() + this.getDensity() * this.g * (dz_m / dx_m));

        J_p = (leftPoint.getPrevPositivePressure() + (this.getDensity() * this.getSpeedWave() * leftPoint.getPrevPositiveVelocity())) - dx_p * Fi_p;
        J_m = (rightPoint.getPrevNegativePressure() - (this.getDensity() * this.getSpeedWave() * rightPoint.getPrevNegativeVelocity())) + dx_m * Fi_m;
        Kv = Math.sqrt((100 - this.flapPosition) / 100) * this.CoefficientApprox;
        if (J_p < J_m) { J_p = J_m;}

        tmpValue1 = 1.0 / (1.0 + Math.sqrt(1.0 + (Kv / (2.0 * this.getDensity() * this.getSpeedWave() * this.getSpeedWave()))) * (J_p - J_m));
        tmpValue2 = (J_p - J_m) / (this.getDensity() * this.getSpeedWave());
        //скорость (положительная и отрицательная составляющие равны)
        currentVelocity = tmpValue1 * tmpValue2;

        //давление (положительное)
        positivePressure = ((J_p + J_m) / 2.0) - (Kv * this.getDensity() * currentVelocity * currentVelocity) / 4.0;// вычисляем текущее давление (положительное)
        //давление (отрицательное)
        negativePressure = ((J_p + J_m) / 2.0) + (Kv * this.getDensity() * currentVelocity * currentVelocity) / 4.0;// вычисляем текущее давление (отрицательное)


        currentPressure = positivePressure;

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
    public ExchangedObject getSafeCopy() {
        Damper returnedDamper=new Damper(this.getDiameter(), this.getLength(), CoefficientApprox);
        returnedDamper.leftObject=leftObject;
        returnedDamper.rightObject=rightObject;
        return returnedDamper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Damper damper = (Damper) o;
        return Double.compare(damper.CoefficientApprox, CoefficientApprox) == 0 &&
                Objects.equals(leftObject, damper.leftObject) &&
                Objects.equals(rightObject, damper.rightObject) &&
                Double.compare(this.getDiameter(), damper.getDiameter()) == 0 &&
                Double.compare(this.getLength(),damper.getLength()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(CoefficientApprox, leftObject, rightObject, this.getDiameter(), this.getLength());
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
    public void receiveCommand(Double value) {
        this.setFlapPosition(value);
    }
}