package com.oilpipelinemodel.app;

// Строитель параллельных насосов
/*
* Пользователь получает данный строитель при вызове метода CreateSerialPump из класса MagistralPipeLineBuilder
* Дальше необходимо создать насосы который будут подключены последовательно
* Для этого есть строитель насосов, пользователь может получить его вызвав метод getPumpBuilder
* После вызова метода commit в PipeLineBuilder созданный насос добавляется в массив Pump в данном классе
* После вызова build создаются последовательно подключенный насосы в том количестве, которое было создано строителем снаружи класса
*
* */

import java.util.ArrayList;

public class SerialPumpBuilder extends aGroupPumpBuilder {


    private MagistralBuilder mB; // магистральный нефтепровод, куда мы будем записывать нашу группу насосов
    private DrainGroupBuilder dplGB; // разветвление
    private DrainPipelineBuilder dpLB; //строитель нитки разветвления (возвращается из DrainGroupBuilder)
    private PumpBuilder pB; //строитель насосов
    private notReturnValveBuilder nRvB; // строитель для обратного клапана
    private GainGroupBuilder gplGB; // сужение
    private GainPipelineBuilder gplB; // строитель нитки сужения (возвращается ищ GainGroupBuilder)
    private PipeLineBuilder plB; // труба для соединения насосов с  обвязкой, потому что не хорошо подключать напрямую сужение сразу к расширению, т.к у нас тогда подключатся все нитки друг к другу

    private ArrayList<Pump> pump = new ArrayList<>();

    SerialPumpBuilder(MagistralBuilder mB)
    {
        this.mB=mB;
        dplGB = new DrainGroupBuilder(mB); // создали строитель для ветвления
        dpLB = dplGB.getDrainGroupBuilder(); // получили строитель для одной ветки ветвления
        pB = new PumpBuilder(this); // строитель для насосов
        nRvB = new notReturnValveBuilder(mB); // строитель для обратного клапана
        gplGB = new GainGroupBuilder(mB); // строитель для сужения
        gplB = gplGB.getGainPipeLineBuilder(); // получили строитель для одной ветки сужения
        plB = new PipeLineBuilder(mB); // строитель нефтепровода
    }   //конструктор скрыт вне пакета


    public PumpBuilder getPumpBuilder(){
        return pB;
    } // вернуть массив строителей каждого насоса

    @Override
    void addPumpObject(Pump pp) {
        pump.add(pp);
    } // массив агрегатов

    public void build()
    {

        for(Pump curr_pump:pump){ // делаем обвязку насосов, созданных строителем
            curr_pump.setNumBranch(0); // назначем номер нитки 0, т.к у нас последовательное соединение насосов


            // Операции повторяющиеся но так получается нагляднее чем выносить это в цикл, к тому же всего 2 ветки всегда
            dpLB.setDiam(curr_pump.GetDiam()); // диаметр ветвления равен диаметру насоса
            dpLB.commit(); // добавили в группу одну ветку ветвления
            // добавляем вторую ветку
            dpLB.setDiam(curr_pump.GetDiam()); // диаметр ветвления равен диаметру насоса
            dpLB.commit(); // добавили в группу одну ветку ветвления



            dplGB.commit(); // собрали ветвление (из 2 ниток)
            mB.addPipeObject(curr_pump); // добавили насос на 0 ветку
            // В этом месте есть ветвление и насос
            // добавляем обратный клапан
            nRvB.setDiam(curr_pump.GetDiam()); // диаметр обратного клапана такой же как у насосоа
            nRvB.setNumBranch(1); // добавляем на нитку параллельную насосу
            nRvB.commit(); // записали

            
            // создаем сужение
            gplB.setDiam(curr_pump.GetDiam()); // 0 ветка
            gplB.commit();

            gplB.setDiam(curr_pump.GetDiam()); // 1 ветка
            gplB.commit();

            gplGB.build(); // собрали сужение
            // добавляем после ветвления участок нефтепровода
            plB.setNumBranch(0); // добавляем в нулевую ветку
            plB.setLenght(10); // 10 метров участок трубы
            plB.setSegmentLen(10); // 10 метров размер одного сегмента, 1 точка для рассчета
            plB.setDiam(curr_pump.GetDiam()); // диаметр будет такой же как и у ветвлений, т.е. равный диаметру насоса
            plB.commit();
            //
            /* итого после каждой итерации у нас будет создано:
            *  - разветвление из 2 веток
            * - насос на 0 ветке
            * - обратный клапан на 1 ветке
            * - сужение 2 веток в одну
            * - трубопровод, подключенный к сужению на 0 ветке, длиной 10 метров
            * */

        }
        pump.clear(); // обязательно очищаем массив насосов после выполнения build
    }
}
