package com.oilpipelinemodel.app.Primitives;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;


abstract class BasePipeObject implements CalculatedObject {

    private double Diameter; // диаметр нефтепровода (есть у любого объекта)
    private long length; // длина объекта (есть у любого объекта)

    ///Коэффициенты трубопровода
    private double Lamda; // Коэффициент гидравлического сопротивления, вычисляем по формуле Альтшуля
    private double epsilon; // коэффициент относительной шероховатости труб
    private double ReinNum; // число Рейнольдса, формуля Альтшуля справедлива для числа Рейнольдса от 10^4 до 10^6
    private double ReinVel; // скорость потока для рассчета числа Рейнольдсаp


    //Формула для вычисления числа Рейнольдса (скорость*диаметр)/(кинематическую вязкость)
    //скорость высчитывается из расхода по формуле Vel=(4*Q)/2*pi*d^2


    private static double speedWave = 1225; // скорость распространения волн в трубопроводе (по сути скорость звука в среде перекачки) полагаем везде одинаковой
    private static double Density = 960; // плотность перекачиваемой жидкости, полагаем везде одинаковой
    private static double StartQ = 10000; // стартовый расход в нефтепроводе м^3/ч


    //Константы (final поэтому не private)
     final double visc = 300; // Вязкость, предположим для простоты среднее значние 300 мм2/с
     final double g = 9.81; //ускорение свободного падения
     final double K = 0.15; // Эквивалентная шероховатость труб, для рассчета коэффициента относительной шероховатости


    protected double getSpeedWave(){
        return speedWave;
    }
    protected double getDensity(){
        return Density;
    }
    protected double getLambda()
    {
        double pow;
        if (this.Diameter>0 && this.visc>0) {
            this.epsilon = (K * Math.pow(10, -3)) / this.Diameter; // относительная шероховатость (10^-3 для перевода из мм в м)
            this.ReinVel = (4 * this.StartQ) / (3600 * Math.PI * Math.pow(this.Diameter, 2)); // пересчитываем скорость из расхода (расход в м3/ч, скорость в м/с)
            this.ReinNum = (this.ReinVel * this.Diameter) / (this.visc * Math.pow(10, -6)); // 10^-6 для перевода из мм2/с в м2/с
        }
        if (this.ReinNum!=0) {
            pow=Math.pow((this.epsilon + (68 / (this.ReinNum))), 0.25);
            this.Lamda = 0.11 * pow; //
        }
        return this.Lamda;

    }
    protected double getDiameter(){return this.Diameter;}
    protected double getEpsilon(){return this.epsilon;}
    protected double getReinNum(){return this.ReinNum;}
    protected long getLength(){return this.length;}
    protected double getStartQ() {return StartQ;}


    public void setDiameter(double diameter) {
        Diameter = diameter;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public static void setSpeedWave(double speedWave) {
        BasePipeObject.speedWave = speedWave;
    }

    public static void setDensity(double density) {
        Density = density;
    }

    public static void setStartQ(double startQ) {
        StartQ = startQ;
    }
}