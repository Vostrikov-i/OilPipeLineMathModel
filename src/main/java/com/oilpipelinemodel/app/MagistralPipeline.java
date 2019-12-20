package com.oilpipelinemodel.app;
/*Магистральный нефтепровод
* Собирается из PipeObject
* Выходные параметры:
* Массив рассчитанных давлений по каждой нитке
* Массив рассчитанных скоростей по каждой нитке
* По сути фасад для объектов нефтепровода
* Пользователь вместо доступа к конкретным объектам нефтепровода
* конструирует магистральный нефтепровод с помощью методов строителя
* */


import java.util.ArrayList;

public class MagistralPipeline{


    private ArrayList<ArrayList<IConnectedPipeObject>> connectionObject = new ArrayList<>(); // массив всех элементов нефтепровода
    private boolean runCalc=false;
    // конструктор скрыт вне пакета
    MagistralPipeline() {
        connectionObject.add(new ArrayList<>()); // создаем одну ветку трубопровода по умолчанию
    }


    /* Метод вычисления давлений нефтепровода
    *  Объекты обсчитываются последовательно, пока не был рассчитан предыдущий, следущий не запускается
    *  Рассчет производится слева направо сверху вниз
    * Первым объектом будет connectionObject(0,0)
    * Вторым connectionObject(1,0)
    * N объектом connectionObject(N,0)
    * N+1  connectionObject(0,1)
    * N+2  connectionObject(1,1)
    * и т.д.
    * Если null в массиве то переходим к следующему объекту
    *
    * */
    public void RunCalc(){
       int maxcountObject=0;
        int maxNumBranch=connectionObject.size(); // количество ниток нефтепровода
        if (maxNumBranch>0){
            maxcountObject=connectionObject.get(0).size(); // все ветки одинакового размера, но возьмем 0
        }
        runCalc=true; // выставляем флаг начала рассчета
        // выставляем начальную точку рассчета
        int numObj=0;
        int numBranch=0;
        ISheduled currentCalcObject=null; // текущий объект для рассчета
        // проходим по массиву объектов
        while(runCalc){
//проверим что объект не null и что у него реализован интерфейс ISheduled (на практике он должен быть у всех, но все же)
            if (connectionObject.get(numBranch).get(numObj)!=null && connectionObject.get(numBranch).get(numObj) instanceof ISheduled){
                currentCalcObject=(ISheduled) connectionObject.get(numBranch).get(numObj);
                currentCalcObject.CalсPressure(); // запускаем рассчет давлений
                //TODO Подумать как сделать поаккуратнее эти гигантские if-else
                if (!currentCalcObject.isRun()){ // если объект не запущен, тогда вычисления закончены
                    if(numBranch<maxNumBranch-1){ // проверяем есть ли еще ветки для рассчета
                        numBranch++;
                    } else{
                        numBranch=0;
                        if (numObj<maxcountObject-1){//проверяем есть ли еще объекты для рассчета
                            numObj++;
                        } else {
                            numObj=0;
                        }
                    }
                }
            } else{ // если не соблюдаются условия переходим к новой ветке
                if(numBranch<maxNumBranch-1){ // проверяем есть ли еще ветки для рассчета
                    numBranch++;
                } else{
                    numBranch=0;
                    if (numObj<maxcountObject-1){//проверяем есть ли еще объекты для рассчета
                        numObj++;
                    } else {
                        numObj=0;
                    }
                }
            }
        }

    }

    void setConfig(ArrayList<ArrayList<IConnectedPipeObject>> connectionObject) {
        this.connectionObject=connectionObject;
    }
    //TODO delete after tests
    public ArrayList<ArrayList<IConnectedPipeObject>> getConnectionMap(){
        return connectionObject;
    }

}
