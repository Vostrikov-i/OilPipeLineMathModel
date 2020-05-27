package com.oilpipelinemodel.app.Primitives;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedPoint;

import java.util.Objects;

/*
* Класс точки сечения нефтепровода
* Любой объект нефтепровода содержит в себе точки сечений
* Точка не знает ничего о том как вычислять свои значения, она получает метод снаружи
* */
//non public
final class PipePoint implements CalculatedPoint {

    private double Pressure;
    private double Velocity;
    private double previousPressure;
    private double previousVelocity;

    //+ и - составляющие давлений
    private double positivePressure;
    private double negativePressure;
    private double prevPositivePressure;
    private double prevNegativePressure;

    // + и - составляющие скоростей
    private double positiveVelocity;
    private double negativeVelocity;
    private double prevPositiveVelocity;
    private double prevNegativeVelocity;

    private double timeCalculated; //время вычисления значений, нужно для адекватного рассчета давлений, т.к. коэффициенты завязаны на скорость звука и период вычислений


    PipePoint(){
    }

    public double getTimeCalculated() {
        return timeCalculated;
    }
    @Override
    public double getPressure() {
        return Pressure;
    }
    @Override
    public double getVelocity(){
        return Velocity;
    }
    @Override
    public double getPreviousPressure(){
        return previousPressure;
    }
    @Override
    public double getPreviousVelocity() {
        return previousVelocity;
    }
    @Override
    public double getPositivePressure() {
        return positivePressure;
    }
    @Override
    public double getNegativePressure() {
        return negativePressure;
    }
    @Override
    public double getPrevPositivePressure() {
        return prevPositivePressure;
    }
    @Override
    public double getPrevNegativePressure() {
        return prevNegativePressure;
    }
    @Override
    public double getPositiveVelocity() {
        return positiveVelocity;
    }
    @Override
    public double getNegativeVelocity() {
        return negativeVelocity;
    }
    @Override
    public double getPrevPositiveVelocity() {
        return prevPositiveVelocity;
    }
    @Override
    public double getPrevNegativeVelocity() {
        return prevNegativeVelocity;
    }


    /* Метод возвращает копию объекта
     *
     * */
    static CalculatedPoint newCalculatedPointInstance(PipePoint pipePoint){

        PipePoint newPoint=new PipePoint();
        newPoint.negativePressure=pipePoint.negativePressure;
        newPoint.negativeVelocity=pipePoint.negativeVelocity;
        newPoint.positivePressure=pipePoint.positivePressure;
        newPoint.positiveVelocity=pipePoint.positiveVelocity;
        newPoint.Pressure=pipePoint.Pressure;
        newPoint.previousPressure=pipePoint.previousPressure;
        newPoint.previousVelocity=pipePoint.previousVelocity;
        newPoint.prevNegativePressure=pipePoint.prevNegativePressure;
        newPoint.prevNegativeVelocity=pipePoint.prevNegativeVelocity;
        newPoint.prevPositivePressure=pipePoint.prevPositivePressure;
        newPoint.prevPositiveVelocity=pipePoint.prevPositiveVelocity;
        newPoint.Velocity=pipePoint.Velocity;
        return newPoint;

    }

    void calc(CalculatedFunction calculatedFunction){
        double[] calculatedData;
        long startTime;
        /*previousPressure=Pressure;
        previousVelocity =Velocity;
        prevNegativePressure=negativePressure;
        prevNegativeVelocity=negativeVelocity;
        prevPositivePressure=positivePressure;
        prevPositiveVelocity=positiveVelocity;*/
        startTime=System.nanoTime();
        calculatedData=calculatedFunction.calcParameters();
        timeCalculated=(System.nanoTime()-startTime)/1000000.0;
        // обновляем текущие данные
        previousPressure=Pressure;
        previousVelocity =Velocity;
        prevNegativePressure=negativePressure;
        prevNegativeVelocity=negativeVelocity;
        prevPositivePressure=positivePressure;
        prevPositiveVelocity=positiveVelocity;
        Pressure=calculatedData[DataOrder.PRESSURE.getIndex()];
        Velocity=calculatedData[DataOrder.VELOCITY.getIndex()];
        positivePressure=calculatedData[DataOrder.POSITIVE_PRESSURE.getIndex()];
        negativePressure=calculatedData[DataOrder.NEGATIVE_PRESSURE.getIndex()];
        positiveVelocity=calculatedData[DataOrder.POSITIVE_VELOCITY.getIndex()];
        negativeVelocity=calculatedData[DataOrder.NEGATIVE_VELOCITY.getIndex()];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PipePoint pipePoint = (PipePoint) o;
        return  Double.compare(pipePoint.Pressure, Pressure) == 0 &&
                Double.compare(pipePoint.Velocity, Velocity) == 0 &&
                Double.compare(pipePoint.previousPressure, previousPressure) == 0 &&
                Double.compare(pipePoint.previousVelocity, previousVelocity) == 0 &&
                Double.compare(pipePoint.positivePressure, positivePressure) == 0 &&
                Double.compare(pipePoint.negativePressure, negativePressure) == 0 &&
                Double.compare(pipePoint.prevPositivePressure, prevPositivePressure) == 0 &&
                Double.compare(pipePoint.prevNegativePressure, prevNegativePressure) == 0 &&
                Double.compare(pipePoint.positiveVelocity, positiveVelocity) == 0 &&
                Double.compare(pipePoint.negativeVelocity, negativeVelocity) == 0 &&
                Double.compare(pipePoint.prevPositiveVelocity, prevPositiveVelocity) == 0 &&
                Double.compare(pipePoint.prevNegativeVelocity, prevNegativeVelocity) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Pressure, Velocity, previousPressure, previousVelocity, positivePressure, negativePressure, prevPositivePressure, prevNegativePressure, positiveVelocity, negativeVelocity, prevPositiveVelocity, prevNegativeVelocity);
    }

    @Override
    public String toString() {
        return "PipePoint{" +
                "previousPressure=" + previousPressure + "\n"+
                ", previousVelocity=" + previousVelocity + "\n"+
                "Pressure=" + Pressure + "\n"+
                ", Velocity=" + Velocity + "\n"+
                ", TimeCalculated=" + timeCalculated + "\n"+
                '}';
    }

}
