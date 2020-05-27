package com.oilpipelinemodel.app.Primitives;
/*
* Класс трубопровода
* У трубопровода может быть более 1 сечения
*
*
* Требования по многопоточному использованию
* метод calcParameters() должен выполняться только одним потоком. Недопустимо парралельное выполнение данного метода несколькими потоками,
* т.к важен порядок вычисления значений точек. Каждая точка должна быть вычислена не раньше предыдущих точек, иначе получатся некорректные данные
* */

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedPoint;
import com.oilpipelinemodel.app.Interfaces.Primitives.ConnectedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ExchangedObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Pipeline extends BasePipeObject implements ConnectedObject, CalculatedObject, ExchangedObject {

    @Nullable
    private ExchangedObject leftObject = null;
    @Nullable
    private ExchangedObject rightObject = null;
    private final List<PipePoint> pipePoints=new ArrayList(Arrays.asList(new PipePoint())); // добавляем одну точку по умолчанию
    // запись в данное поле чтобы избежать ошибок получать через метод setNumCalcCutPoint
    private int numCalcCutPoint=0; //номер рассчитываемого сечения

    public Pipeline(double Diameter, long length){
        this.setDiameter(Diameter);
        this.setLength(length);

    }

    public void addCutPoint(){
        pipePoints.add(new PipePoint());
    }

    private void setNumCalcCutPoint(int num){
        boolean isCorrect=!pipePoints.isEmpty() & num<pipePoints.size() & num>=0;
        if(isCorrect){
            this.numCalcCutPoint=num;
        } else {
            throw  new IllegalArgumentException("Uncorrect cut number");
        }
    }

    public int getCutPointsCount(){
        return pipePoints.size();
    }


    @Override
    public synchronized void calcParameters(){
        setNumCalcCutPoint(0); // начинаем вычисления с 1 точки сечения
        for (PipePoint pipePoint: pipePoints) {
            pipePoint.calc(this::refreshPointsValue); // запускаем расчет по одному сечению
        }
        setNumCalcCutPoint(0); // сделаем сброс вычисляемой точки на всякий случай
    }


    private double[] refreshPointsValue() {
        double timeCalculated=pipePoints.get(numCalcCutPoint).getTimeCalculated();
        double[] returnedValue=new double[DataOrder.getMaxIndex()];
        double dz_m = 0;//this.GetZValue(numPoint + 1) - this.GetZValue(numPoint);
        double dz_p = 0;//this.GetZValue(numPoint)-this.GetZValue(numPoint-1);
        double dx_m =getLength()/(getCutPointsCount()+1);//(timeCalculated<0.0001)?(this.getLength()/(this.getCutPointsCount()+1)):((timeCalculated*1000000)*this.getSpeedWave());//this.getLength()/(this.getCutPointsCount()+1);//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double dx_p =getLength()/(getCutPointsCount()+1);// (timeCalculated<0.0001)?(this.getLength()/(this.getCutPointsCount()+1)):((timeCalculated*1000000)*this.getSpeedWave());// this.getLength()/(this.getCutPointsCount()+1);//(d_t/1000)*this.GetSpeedWave();// this.GetSegmentLen(); // по сути длина сегмента
        double Fi_m;
        double Fi_p;
        double J_m;
        double J_p;
        double currentPressure;
        double currentVelocity;

        CalculatedPoint leftPoint= getLeftPoint(numCalcCutPoint);
        CalculatedPoint rightPoint=getRightPoint(numCalcCutPoint);
        if (numCalcCutPoint<pipePoints.size()-1) {setNumCalcCutPoint(numCalcCutPoint+1);} //прибавляем индекс точки, для перехода к следующем итерации

        if (leftPoint==null || rightPoint==null){ // нет левой или правой точки, вернем нулевой массив,т.к. физически это верно
            return new double[DataOrder.getMaxIndex()];
        }

        Fi_p = (this.getLambda() * this.getDensity() * leftPoint.getPrevPositiveVelocity() * Math.abs(leftPoint.getPrevPositiveVelocity()));
        Fi_p = Fi_p / (2 * this.getDiameter() + this.getDensity() * this.g * (dz_p / dx_p));

        Fi_m = (this.getLambda() * this.getDensity() * rightPoint.getPrevNegativeVelocity() * Math.abs(rightPoint.getPrevNegativeVelocity()));
        Fi_m = Fi_m / (2 * this.getDiameter() + this.getDensity() * this.g * (dz_m / dx_m));
        J_p = (leftPoint.getPrevPositivePressure() + (this.getDensity() * this.getSpeedWave() * leftPoint.getPrevPositiveVelocity())) - dx_p * Fi_p;
        J_m = (rightPoint.getPrevNegativePressure() - (this.getDensity() * this.getSpeedWave() * rightPoint.getPrevNegativeVelocity())) + dx_m * Fi_m;

        currentPressure=(J_p+J_m)/2;
        currentVelocity=(J_p - J_m) / (2 * this.getDensity() * this.getSpeedWave());

        // + и - составляющие равны
        returnedValue[DataOrder.PRESSURE.getIndex()]=currentPressure/1000000; //в МПа
        returnedValue[DataOrder.VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.POSITIVE_PRESSURE.getIndex()]=currentPressure;
        returnedValue[DataOrder.NEGATIVE_PRESSURE.getIndex()]=currentPressure;
        returnedValue[DataOrder.POSITIVE_VELOCITY.getIndex()]=currentVelocity;
        returnedValue[DataOrder.NEGATIVE_VELOCITY.getIndex()]=currentVelocity;
        return returnedValue;
    }


    private CalculatedPoint getLeftPoint(int index)
    {
        CalculatedPoint returnedValue=null;
        if(index>-1 && index<getCutPointsCount()-1) { //0 и положительный индексы
            switch (index == 0 ? 1 : 0) {
                case 0 -> returnedValue = pipePoints.get(index - 1);
                case 1 -> returnedValue = (leftObject != null) ? leftObject.getLastPoint() : null;
            }
        }
        return returnedValue;
    }

    private CalculatedPoint getRightPoint(int index)
    {
        CalculatedPoint returnedValue=null;
        if(index>=0 && index<getCutPointsCount()) {
            switch (index != (getCutPointsCount()-1) ? 1 : 0) {
                case 0 -> returnedValue = (rightObject != null) ? rightObject.getLastPoint() : null;
                case 1 -> returnedValue = pipePoints.get(index);
            }
        }
        return returnedValue;
    }


    @Override
    public CalculatedPoint getFirstPoint() {
        return (!pipePoints.isEmpty())? PipePoint.newCalculatedPointInstance(pipePoints.get(0)):null;
    }

    @Override
    public CalculatedPoint getLastPoint() {
        return (!pipePoints.isEmpty())?PipePoint.newCalculatedPointInstance(pipePoints.get(getCutPointsCount()-1)):null;
    }

    @Override
    public void setLeftObject(ExchangedObject exchangedObject) {
        leftObject = exchangedObject;
    }

    @Override
    public void setRightObject(ExchangedObject exchangedObject) {
        rightObject = exchangedObject;
    }

    @Override
    public String toString() {
     String str= "Pipeline{"+"\n";
     for(PipePoint pipePoint: pipePoints){
         str=str+pipePoint.toString()+"\n";
     }
     return str+"}";
    }

    @Override
    public ExchangedObject getSafeCopy() {
        Pipeline returnedPipeline=new Pipeline(this.getDiameter(), this.getLength());
        returnedPipeline.leftObject=leftObject;
        returnedPipeline.rightObject=rightObject;
        return returnedPipeline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pipeline pipeline = (Pipeline) o;
        return  Objects.equals(leftObject, pipeline.leftObject) &&
                Objects.equals(rightObject, pipeline.rightObject) &&
                Double.compare(this.getDiameter(), pipeline.getDiameter())==0 &&
                Double.compare(this.getLength(), pipeline.getLength())==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftObject, rightObject, this.getDiameter(), this.getLength());
    }
}