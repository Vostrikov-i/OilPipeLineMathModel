package com.oilpipelinemodel.app;

import java.util.ArrayList;

public class DrainGroupBuilder extends aPipeObjectBuilder {

    private DrainPipelineBuilder dPlB=new DrainPipelineBuilder(this);
    private MagistralBuilder mB;
    private PipeLineBuilder pB;
    private final int startBranch=0; // номер стартовой ветки (0 - первая ветка) (final потому что вдруг потом захочется переделать конструктор, но сейчас не надо, чтобы startBranch был не равен 0 )
    private final long segmentLenght=5; // константы, т.к в целом разбивать более чем на 1 сегмент есть смысл только для нефтепровода, но длина нужна, для приведения рассчетов к реальному времени
    private final long lenght=5;
    private ArrayList<DrainPipeLine> adpL=new ArrayList<>();

    DrainGroupBuilder(MagistralBuilder mB)
    {
        this.mB=mB;
        pB=new PipeLineBuilder(this.mB);
    }

    void addBranchedObject(DrainPipeLine dpL)
    {
        adpL.add(dpL);
    }

    DrainPipelineBuilder getDrainGroupBuilder(){
        return dPlB;
    }
    @Override
    public void commit()
    {
        // делаем привязки Top и Down объектов
        int numBranch=this.startBranch;
        DrainPipeLine curr_drain;
        DrainPipeLine added_drain=new DrainPipeLine();
        // модернизируем массив adpL для того чтобы с ним мог работать метод AddDrainPair()

        for(DrainPipeLine cDpl: adpL) {
            cDpl.setNumBranch(numBranch);
            cDpl.SetSegmentLen(segmentLenght);
            cDpl.SetPipeLen(lenght);
            numBranch++; //расставили всем добавленным ветвления номера ниток
        }
        if (adpL.size()>2){ //ветвление больше чем из 2 ниток
            for (int i=2;i<adpL.size();i+=2){ //надо добавить еще элементов DrainPipeLine т.к. по сути у нас ветвление из N веток делается с помощью N-1 ветвлений по 2 ветки (первые 2 элемента нас не интересуют)
                curr_drain=adpL.get(i);
                added_drain.setNumBranch((curr_drain.getNumBranch()-1));
                added_drain.SetDiam(adpL.get(i-1).GetDiam()); // диаметр от объекта
                added_drain.SetPipeLen(curr_drain.GetPipeLen()); // длина участка от текущего объекта
                added_drain.SetSegmentLen(curr_drain.GetSegmentLen());
                adpL.add(i, added_drain); //не очень оптимально, но массивы не большие, и не могут в условиях текущей задачи быть слишком большими, поэтому проигнорируем это и не будем все переводить на LinkedList
            }
        }

        AddDrainPair(new ArrayList<>());

        adpL.clear(); // после создания ветвления обнулим массив ниток ветвления


    }
    //метод для реккуретного добавления пар ветвлений
    // До  вызова данного метода массив ветвлений должен быть корректно сформирован по следующим правилам
    /*
    *  Если добавляется больше 2 веток, тогда:
    *
    *
    * */
    private void AddDrainPair(ArrayList<DrainPipeLine> addedDrainPipeLine){
        DrainPipeLine top_object;
        DrainPipeLine down_object;
        double diam;
        long lenght;
        long segmentLenght;
        while (adpL.size()>0){ // если в массиве что то есть тогда продолжаем
            top_object=adpL.get(0);
            down_object=adpL.get(1); //в идеальном мире в adpL не может остаться 1 элемент, но если останется здесь поймаем исключение
            top_object.setDownObject(down_object);
            down_object.setTopObject(top_object); // связали объекты друг с другом
            mB.addPipeObject(top_object); // добавили ветвления в карту
            mB.addPipeObject(down_object);

            if(addedDrainPipeLine.size()>0){ // если что то есть в массиве, тогда надо добавлять кусок трубы (PipeLine)
                for(DrainPipeLine prev_dpl:addedDrainPipeLine){
                    diam=prev_dpl.GetDiam();
                    segmentLenght=top_object.GetSegmentLen();
                    lenght=top_object.GetPipeLen();
                    pB.setDiam(diam);
                    pB.setLenght(lenght);
                    pB.setSegmentLen(segmentLenght);
                    pB.setNumBranch(prev_dpl.getNumBranch());
                    pB.commit(); // добавили трубу нефтепровода в карту
                }
            }

            addedDrainPipeLine.add(top_object); // добавили верхний объект для того чтобы потом добавлять PipeLine в соответсвующие ветки

            adpL.remove(1); //удалили сначала 1
            adpL.remove(0); // потом удалим нулевой
            //ну или 2 раза удалить нулевой, но так по моему нагляднее получается

            AddDrainPair(addedDrainPipeLine);
        }
        return;

    }

}
