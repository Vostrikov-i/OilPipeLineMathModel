package com.oilpipelinemodel.app.Utils;

import com.oilpipelinemodel.app.Interfaces.Util.ForkMediatory;
import com.oilpipelinemodel.app.Interfaces.Primitives.ForkedObject;

import java.util.ArrayList;
import java.util.List;

/*Класс посредник для взаимодействия ветвлений
* работает с 2 ветками
*
* */
public class ForkTwoBranchMediator implements ForkMediatory {

    private final List<ForkedObject> forkedObjects=new ArrayList<>();

    @Override
    public void appendFork(ForkedObject forkedObject) {
        if (forkedObjects.size()>=2){
            //выкинем исключение при попытке добавить более 2 веток для посредника,
           throw new UnsupportedOperationException("Uncorrected forked object count. Must be no more than 2");
        }
        forkedObjects.add(forkedObject);
    }

    @Override
    public ForkedObject[] getLinkedForkData(ForkedObject forkedObject) {
        //возвращает все объекты которые хранит ForkMediator кроме forkedObject
        return forkedObjects.stream().filter(x->x!=forkedObject).toArray(ForkedObject[]::new);
    }
}
