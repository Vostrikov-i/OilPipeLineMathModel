package com.oilpipelinemodel.app;

import java.util.ArrayList;
import java.util.List;

/*
* Строитель магистрального нефтепровода
* Здесь нет абстрактного класса для него, потому что в нем нет смысла,
* строитель только одного типа, поэтому он и не нужен
* Строитель имеет методы создания отдельных объектов нефтепровода, таких как участок трубы, стартовый и конечный резервуар, НПС и т.д
*
*
* */

public class MagistralBuilder {

    private MagistralPipeline mP=new MagistralPipeline();
    private PipeLineBuilder pB;
    private STankBuilder stB;
    private ETankBuilder etB;
    private GainPipelineBuilder gplB;
    private DrainPipelineBuilder dplB;
    private PumpBuilder pmpB;
    private ArrayList<ArrayList<IConnectedPipeObject>> connectionObject = new ArrayList<>(); // конструируемый массив
    private ArrayList<Boolean> needNulladd = new ArrayList<>(); // флаги того, что соседние ветки надо дополнять null значениям, устанавливается когда в соседнюю ветку добавлен Gain и снимается когда добавляется Drain

    public MagistralBuilder()
    {
        connectionObject.add(new ArrayList<>()); // создаем одну ветку трубопровода по умолчанию
        needNulladd.add(false); // для нулевой ветки всегда false
         pB = new PipeLineBuilder(this);
         stB = new STankBuilder(this);
         etB = new ETankBuilder(this);
        gplB = new GainPipelineBuilder(this);
        dplB = new DrainPipelineBuilder(this);
        pmpB=new PumpBuilder(this);

    }

    public PipeLineBuilder CreatePipeLine()
    {
        return pB;
    }
    public STankBuilder CreateStartTank()
    {
        return stB;
    }
    public ETankBuilder CreateEndTank()
    {
        return etB;
    }
    public GainPipelineBuilder CreateGainPipeline(){return gplB;}
    public DrainPipelineBuilder CreateDrainPipeline(){return dplB;}
    public PumpBuilder CreatePump(){return pmpB;}

    public void setPipeLineList(List<PipelineObject> pipelineObjectList){

    };

    public void setTankList(List<TankObject> tankObjectList){

    };

    public void setPumpList(List<PumpObject> pumpObjectList){

    };

    public MagistralPipeline build()
    {
        mP.setConfig(connectionObject); // отдали нефтепроводу сформированную конфигурации
        return mP; // возвращаем готовый объект нефтепровода
    }

    /* Все массивы являются двумерными
     *  1 индекс массива это номер ветки в которую добавляется элемент
     * 2 индекс это номер элемента в этой ветке
     *
     * Добавление элемента в текущую ветку автоматически соединяет его слева с пердыдущим и предыдущий соединяет справа с текущим
     * Поэтому при добавлении новой ветки (возможно только через ветвление) в нефтепровод туда добавляется N-ое количество null объектов, где N это размер предыдущей ветки
     * Т.е. вид массива примерно такой (для нефтепровода с насосом и байпасной трубой)
     *
     * StartTank, PipeLine, PipeLine, DrainPipeline, Pump,     GainPipeline, PipeLine, EndTank
     * null,      null,     null,     DrainPipeline, Pipeline, GainPipeline, null,     null
     *
     * Описание массивов:
     * connectionObject - все соединенные друг с другом объекты (не возвращается наружу)
     * managedObject - все управляемые снаружи объекты (вовзращается наружу)
     * typeObject - массив из описаний объектов, содержит в себе объекты реализующие интерфейс IPipeObject, чтобы пользователь мог получить информацию о текущей конфигурации нефтепровода и о настройках каждого элемента (возвращается наружу)
     *
     * ОБРАТИТЬ ВНИМАНИЕ:
     * интерфейс IConnectedPipeObject унаследован от интерфейса ICalculatedPipeObject поэтому IConnectedPipeObject это сделано по нескольким причинам
     * 1. Поскольку интерфейс IConnectedPipeObject подразумевает реализацию ICalculatedPipeObject, значит мы будет 100% уверены, что соединяем объекты которые в состоянии посчитать свое давление
     * 2. Пользователю не нужен IConnectedPipeObject, однако ему неплохо дать ICalculatedPipeObject, таким образом мы сможем вернуть массив IConnectedPipeObject как массив ICalculatedPipeObject не думаю о преобразованиях типа
     *
     * */

    // метод добавления объекта к нефтепроводу, в конечном итоге его вызывают все строители объектов нефтепровода
    long addPipeObject(IConnectedPipeObject add_obj) {
        int currNumBranch;
        long current_size=0;
        currNumBranch = add_obj.getNumBranch();

        if (currNumBranch >= 0) { // будем делать манипуляции все, только если currNumBranch положительная

            // будем добавлять новые ArrayList в connectionObject пока номер ветки
            if (add_obj instanceof DrainPipeLine) { // Делаем этим манипуляции только для объектов DrainPipeLine, ветвление может начинаться только с него
                /* ветки нумеруются с 0, размер массива с 1. Нужно для защиты от добавления 3 ветки без добавления 2
                 * корректность и очередность добавления веток при ветвлении на совести соответствующих строителей
                 * если они не работают корректно это их проблемы, мы просто проигнорируем то что они нам дали и выпадем с исключением
                 * или вернем false
                 * */
                if (currNumBranch == connectionObject.size()) {
                    ArrayList<IConnectedPipeObject> tmpArr = new ArrayList<>();
                    for (int i = 0; (i < connectionObject.get(currNumBranch - 1).size() - 1); i++) {
                        tmpArr.add(null); // заполняем null, т.к массив новой ветки должен быть по размеру равен массиву предыдущей ветки, это важно!!!
                    }
                    // здесь массив новой ветки будет по размеру на 1 элемент меньше массива предыдущей ветки
                    connectionObject.add(tmpArr);
                    needNulladd.add(false); // поскольку DrainPipeLine, то добавляем false
                    tmpArr = null; // обнулим на всякий случай, для сборщика мусора
                }
            }

            // Проверяем что не обратимся к несуществующему элементу массива, ведь мы проигнорировали добавление нового массива при некорректном номере ветки
            if (currNumBranch < connectionObject.size()) {
                int brNum=add_obj.getNumBranch();
                if (add_obj instanceof GainPipeLine && brNum > 0) { // при добавлении сужения надо установить признак того, что эту ветку (за исключением нулевой) надо дополнять null значениями
                    needNulladd.set(brNum, true);
                }
                if (add_obj instanceof DrainPipeLine) { // при добавлении расширения, установим признак того, что эту ветку и соседнюю снизу, если она есть не надо дополнять null значениями (соседнюю не надо дополнять Null потому что, мы для того и сделали ветвление чтобы на нее выйти)
                    needNulladd.set(brNum, false);
                    if (needNulladd.size()>brNum+1){ // соседняя ветка существует
                        needNulladd.set(brNum+1, false);
                    }
                }

                IConnectedPipeObject leftObj = null;
                if (connectionObject.get(currNumBranch).size() > 0) {
                    leftObj = connectionObject.get(currNumBranch).get((connectionObject.get(currNumBranch).size() - 1)); // получили последний элемент в данной ветке
                }
                // если слева есть реальный объект (как мы помним мы не зря заполняли null массивы)
                if (leftObj != null) {
                    leftObj.setRightObject(add_obj); // добавили ему ссылку на элемент справа
                    connectionObject.get(currNumBranch).set((connectionObject.get(currNumBranch).size() - 1), leftObj); // обновили массив
                    add_obj.setLeftObject(leftObj); // добавили добавляемому элементу ссылку на элемент слева
                }
                connectionObject.get(currNumBranch).add(add_obj); // теперь спокойно добавляем новый элемент
                // проверим, если есть массивы других веток и их размеры меньше текущей ветки, то их надо доравнять до размера текущего заполнив недостающие null значениями
                 current_size = connectionObject.get(currNumBranch).size();

                for (int i = 0; i < connectionObject.size(); i++) { // проходим по листам connectionObject
                    if (needNulladd.get(i)) { // если у ветки есть признак того, что ее надо дополнять Null
                        int edit_size = connectionObject.get(i).size();
                        while (edit_size < current_size) {
                            connectionObject.get(i).add(null); // добавляем null объект
                            edit_size = connectionObject.get(i).size();
                        }
                    }
                }
            }
        }
        return (current_size-1); // size() вовзращает размер массива с 1, а индекс добавленного элемента на 1 меньше, в случае неудачи вернем -1
    }

}
