package com.oilpipelinemodel.app.Primitives;

import com.oilpipelinemodel.app.Interfaces.Util.ForkMediatory;
import com.oilpipelinemodel.app.Interfaces.Primitives.*;
import com.oilpipelinemodel.app.Utils.ParametersSaver;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/*
* разветвеление на 2 ветки
* разветвеление на большее количество веток требует других алгоритмов рассчета
* данные между ветками для рассчета передаются через ForkMediator
*
* */
public final class ForkPipeline extends BasePipeObject implements ConnectedObject, CalculatedObject, ForkedObject, ExchangedObject {
    @Nullable
    private ExchangedObject leftObject = null;
    @Nullable
    private ExchangedObject rightObject = null;
    private final PipePoint pipePoint=new PipePoint();
    private final ForkMediatory forkMediator;
    private final ParametersSaver<Double> FiPositive=new ParametersSaver<Double>("Fi positive", 0.0);
    private final ParametersSaver<Double> FiNegative=new ParametersSaver<Double>("Fi negative",0.0);
    private final ParametersSaver<Double> JPositive=new ParametersSaver<Double>("J positive",0.0);
    private final ParametersSaver<Double> JNegative=new ParametersSaver<Double>("J Negative",0.0);

    public ForkPipeline(double Diameter, long length, ForkMediatory forkMediator){
        this.setDiameter(Diameter);
        this.setLength(length);
        this.forkMediator=forkMediator;
        this.forkMediator.appendFork(this);
    }

    private CalculatedPoint getLeftPoint()
    {
        return (leftObject!=null)?leftObject.getLastPoint():null;
    }

    private CalculatedPoint getRightPoint()
    {
        return (rightObject!=null)?rightObject.getFirstPoint():null;
    }


    private double[] refreshPointsValue()
    {
        double[] returnedValue=new double[DataOrder.getMaxIndex()];
        ForkedObject[] linkedObject=forkMediator.getLinkedForkData(this);
        if (linkedObject.length==0 || linkedObject.length>1) { // если нет связанных объектов или их больше одного, то выкинем исключение, посчитать все равно не сможет
            throw new UnsupportedOperationException("Uncorrected fork linked objects count. Must be 1");
        }
        double dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
        double dz_p = 0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
        double dx_m = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double dx_p = this.getLength();//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double Fi_m;
        double Fi_p;
        double J_m;
        double J_p;
        double JpLinked=linkedObject[0].getJPositive();
        double JnLinked=linkedObject[0].getJNegative();
        double FiPLinked=linkedObject[0].getFiPositive();
        double FiNLinked=linkedObject[0].getFiNegative();
        double currentPressure;
        double currentVelocity;
        double positivePressure;
        double negativePressure;
        double positiveVelocity;
        double negativeVelocity;
        CalculatedPoint linkedCutPoint=linkedObject[0].getCutPoint();

        CalculatedPoint leftPoint=getLeftPoint();
        CalculatedPoint rightPoint=getRightPoint();
        if (rightPoint==null){ // если нет правой точки, вернем нулевой массив,т.к. физически это верно все будет вытекать в открытое пространство
            return new double[DataOrder.getMaxIndex()];
        }
        if (leftPoint!=null) //нет левой точки, значит это не первая нитка нефтепровода
        {
            Fi_p = (this.getLambda() * this.getDensity() * leftPoint.getPrevPositiveVelocity()*Math.abs(leftPoint.getPrevPositiveVelocity()));
            Fi_p=Fi_p/(2*this.getDiameter()+this.getDensity()*this.g*(dz_p/dx_p));
            Fi_m = (this.getLambda() * this.getDensity() * rightPoint.getPrevNegativeVelocity()*Math.abs(rightPoint.getPrevNegativeVelocity()));
            Fi_m=Fi_m/(2*this.getDiameter()+this.getDensity()*this.g*(dz_m/dx_m));
            J_p = (leftPoint.getPrevPositivePressure() + (this.getDensity() * this.getSpeedWave() * leftPoint.getPrevPositiveVelocity())) - dx_p * Fi_p;
            J_m = (rightPoint.getPrevNegativePressure() - (this.getDensity() * this.getSpeedWave() * rightPoint.getPrevNegativeVelocity())) + dx_m * Fi_m;
            positivePressure =(J_p+J_m+JnLinked)/3.0;
            negativePressure = positivePressure;
            positiveVelocity =(positivePressure -J_m)/(this.getDensity() * this.getSpeedWave());
        }
        else {
            Fi_m = (this.getLambda() * this.getDensity() * rightPoint.getPrevNegativeVelocity()*Math.abs(rightPoint.getPrevNegativeVelocity()));
            Fi_m=Fi_m/(2*this.getDiameter()+this.getDensity()*this.g*(dz_m/dx_m));
            J_m = (rightPoint.getPrevNegativePressure() - (this.getDensity() * this.getSpeedWave() * rightPoint.getPrevPositiveVelocity())) + dx_m * Fi_m;
            positivePressure =(JpLinked+J_m+JnLinked)/3.0;
            negativePressure = positivePressure;
            positiveVelocity =(linkedCutPoint.getPositivePressure()-J_m)/(this.getDensity() * this.getSpeedWave());
        }
        negativeVelocity = positiveVelocity +linkedCutPoint.getPositiveVelocity();
        currentPressure= positivePressure;
        currentVelocity=positiveVelocity;

        // + и - составляющие давлений равны, скорости отличаются
        returnedValue[DataOrder.PRESSURE.getIndex()]=currentPressure/1000000; //в МПа
        returnedValue[DataOrder.VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.POSITIVE_PRESSURE.getIndex()]=positivePressure;
        returnedValue[DataOrder.NEGATIVE_PRESSURE.getIndex()]=negativePressure;
        returnedValue[DataOrder.POSITIVE_VELOCITY.getIndex()]=positiveVelocity;
        returnedValue[DataOrder.NEGATIVE_VELOCITY.getIndex()]=negativeVelocity;

        return returnedValue;
    }



    @Override
    public synchronized void calcParameters() {
        pipePoint.calc(this::refreshPointsValue); // запускаем расчет по одному сечению
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
        ForkPipeline returnedForkPipeline = new ForkPipeline(this.getDiameter(), this.getLength(), forkMediator);
        returnedForkPipeline.leftObject=leftObject;
        returnedForkPipeline.rightObject=rightObject;
        return returnedForkPipeline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForkPipeline forkPipeline = (ForkPipeline) o;
        return leftObject.equals(forkPipeline.leftObject) &&
                rightObject.equals(forkPipeline.rightObject) &&
                forkMediator.equals(forkPipeline.forkMediator) &&
                Double.compare(this.getDiameter(), forkPipeline.getDiameter())==0 &&
                Double.compare(this.getLength(), forkPipeline.getLength())==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftObject, rightObject, forkMediator, this.getDiameter(), this.getLength());
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
    public double getFiPositive() {
        return FiPositive.getParameter().doubleValue();
    }

    @Override
    public double getFiNegative() {
        return FiNegative.getParameter().doubleValue();
    }

    @Override
    public double getJPositive() {
        return JPositive.getParameter().doubleValue();
    }

    @Override
    public double getJNegative() {
        return JNegative.getParameter().doubleValue();
    }

    @Override
    public CalculatedPoint getCutPoint() {
        return PipePoint.newCalculatedPointInstance(pipePoint); // возвращаем копию точки давлений
    }
}
