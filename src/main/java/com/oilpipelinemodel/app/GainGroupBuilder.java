package com.oilpipelinemodel.app;

import java.util.ArrayList;

public class GainGroupBuilder {

    private GainPipelineBuilder gPlB=new GainPipelineBuilder(this);
    private MagistralBuilder mB;
    private final int startBranch=0; // номер стартовой ветки (0 - первая ветка)
    private PipeLineBuilder pB;
    private final long segmentLenght=5; // константы, т.к в целом разбивать более чем на 1 сегмент есть смысл только для нефтепровода, но длина нужна, для приведения рассчетов к реальному времени
    private final long lenght=5;
    private ArrayList<GainPipeLine> agpL=new ArrayList<>();
    private IBranched prev_obj=null;

    public GainGroupBuilder(MagistralBuilder mB)
    {

        this.mB=mB;
        pB=new PipeLineBuilder(this.mB);
    }
    // вернуть строителя одной нитки
    public  GainPipelineBuilder getGainPipeLineBuilder() {
        return gPlB;
    }

    void addBranchedObject(GainPipeLine gpL)
    {
        agpL.add(gpL);
    }

    public void build()
    {
        // делаем привязки Top и Down объектов
        int numBranch=this.startBranch;
        GainPipeLine curr_gain;
        GainPipeLine added_gain=new GainPipeLine();
        // модернизируем массив adpL для того чтобы с ним мог работать метод AddDrainPair()

        for(GainPipeLine cGpl: agpL) {
            cGpl.setNumBranch(numBranch);
            cGpl.SetSegmentLen(segmentLenght);
            cGpl.SetPipeLen(lenght);
            numBranch++; //расставили всем добавленным ветвления номера ниток
        }
        if (agpL.size()>2){ //ветвление больше чем из 2 ниток
          for (int i=agpL.size()-2;i>=1;i--){ // проходим по массиву ветвлений и добавляем туда дополнительные GainPipeline
              curr_gain=agpL.get(i);
              added_gain.setNumBranch(curr_gain.getNumBranch());
              added_gain.SetDiam(curr_gain.GetDiam()); // диаметр от объекта
              added_gain.SetPipeLen(agpL.get(i+1).GetPipeLen()); // длина участка от объекта следующей нитки
              added_gain.SetSegmentLen(agpL.get(i+1).GetSegmentLen());
              agpL.add(i+1, added_gain); //не очень оптимально, но массивы не большие, и не могут в условиях текущей задачи быть слишком большими, поэтому проигнорируем это и не будем все переводить на LinkedList

          }
        }

        AddGainPair(new ArrayList<>());

        agpL.clear(); // после создания ветвления обнулим массив ниток ветвления


    }
    //метод для реккуретного добавления пар ветвлений
    // До  вызова данного метода массив ветвлений должен быть корректно сформирован по следующим правилам
    /*
     *  Если добавляется больше 2 веток, тогда:
     *
     *
     * */
    private void AddGainPair(ArrayList<GainPipeLine> addedGainPipeLine){
        GainPipeLine top_object;
        GainPipeLine down_object;
        double diam;
        long lenght;
        long segmentLenght;
        while (agpL.size()>0){ // если в массиве что то есть тогда продолжаем
            //здесь начинаем с конца массива а не сначала
            top_object=agpL.get(agpL.size()-2); //в идеальном мире в agpL не может остаться 1 элемент, но если останется здесь поймаем исключение
            down_object=agpL.get(agpL.size()-1);
            top_object.setDownObject(down_object);
            down_object.setTopObject(top_object); // связали объекты друг с другом

            // добавляем нижний объект
            mB.addPipeObject(down_object);



            if(addedGainPipeLine.size()>0){ // если что то есть в массиве, тогда надо добавлять кусок трубы (PipeLine)
                for(GainPipeLine prev_gpl:addedGainPipeLine){
                    diam=top_object.GetDiam();
                    segmentLenght=prev_gpl.GetSegmentLen();
                    lenght=prev_gpl.GetPipeLen();
                    pB.setDiam(diam);
                    pB.setLenght(lenght);
                    pB.setSegmentLen(segmentLenght);
                    pB.setNumBranch(top_object.getNumBranch());
                    pB.commit(); // добавили трубу нефтепровода в карту
                }
            }
            // верхний объект добавится только после формирования нефтепровода на этой нитке
            mB.addPipeObject(top_object);
            addedGainPipeLine.add(0, top_object); // добавили верхний объект для того чтобы потом добавлять PipeLine в соответсвующие ветки (добавляем в верхний элемент!!!)

            agpL.remove(agpL.size()-2); //удалили сначала предпоследний
            agpL.remove(agpL.size()-1); // потом удалим последний
            //ну или 2 раза удалить последний, но так по моему нагляднее получается

            AddGainPair(addedGainPipeLine);
        }
        return;

    }
}
