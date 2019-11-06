package com.oilpipelinemodel.app;

/*
* Класс строителя насосов, насосы строятся только группой (или параллельные или последовательные) нельзя создать одиночный насос
* Работает всеэто следующим образом
* Пользователь вызывает строитель унаследованный от абстрактного класса aGroupPumpBuilder (главное это наличие метода addPumpObject)
* Получает массив строителей для конкретного насоса
* Метод build вызванный для строителя насоса, добавляет насос в private массив насосов в строителе группы насосов
* В зависимости от типа строителя (параллельно или последовательно) насосам устанавливаются номера ниток нефтепровода, плюс делается вся обвязка, (ветвления, обратные клапаны)
* При вызове метода build группового строителя насосы и вся обвязка добавляется в карту объектов нефтепровода, которая хранится в классе MagistralPipeLine
* Т.е. пользователь снаружи может задать только диаметры насосов, их количество, коэффициенты аппроксимации для каждого насососа, остальное создается внутри строителя без участия пользователя
*
* Почему групповые строители унаследованы от абстрактного класса, а не реализуют интерфейс? Потому что метод addPumpObject для группового строителя не должен быть доступен пользователю снаружи пакета,
* однако при реализации этого метода через интерфейс нельзя его сделать не Public. Формально для вызова addPumpObject ему нужно передать экземлпяр класса Pump, который скрыт вне пакета, но наличие
* public метода в который нужно передать параметр с default видимостью не лучшее решение
* */

public class PumpBuilder {

    private double diam=0.2;
    private double maxSpeed=3000;
    private double coeffA=305.0;
    private double coeffB=0.00000208;
    private aGroupPumpBuilder gPB;
    private final long segmentLenght=1; // константы, т.к в целом разбивать более чем на 1 сегмент есть смысл только для нефтепровода, но длина нужна, для приведения рассчетов к реальному времени
    private final long lenght=1;

    // конструктор скрыть вне пакета
    PumpBuilder(aGroupPumpBuilder gPB)
    {
        this.gPB=gPB;
    }

    public void setDiam(double diam) { this.diam=diam; }

    public void commit() {
        Pump pp=new Pump();
        pp.setApprox_a(coeffA);
        pp.setApprox_b(coeffB);
        pp.SetDiam(diam);
        pp.setMax_speed(maxSpeed);
        pp.SetPipeLen(lenght);
        pp.SetSegmentLen(segmentLenght);
        gPB.addPumpObject(pp);
    }
    public void setMaxSpeed(double maxSpeed){
        this.maxSpeed=maxSpeed;
    }
    public void setApproxCoeffA(double coeffA){
        this.coeffA=coeffA;
    }
    public void setApproxCoeffB(double coeffB){
        this.coeffB=coeffB;
    }
}

