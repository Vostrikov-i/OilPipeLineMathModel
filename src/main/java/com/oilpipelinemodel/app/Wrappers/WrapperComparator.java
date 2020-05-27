package com.oilpipelinemodel.app.Wrappers;

import com.oilpipelinemodel.app.Interfaces.Wrappers.Wrappers;

import java.util.Comparator;

public class WrapperComparator implements Comparator<Wrappers> {

    @Override
    public int compare(Wrappers o1, Wrappers o2) {

            if (o1.getPosition().getBranchPosition()!=o2.getPosition().getBranchPosition()){ //элемент с большей позицией однозначно больше
                return o1.getPosition().getBranchPosition()>o2.getPosition().getBranchPosition()?1:-1;
            }
            if (o1.getPosition().getBranch()!=o2.getPosition().getBranch()){ //элемент с большим номером ветки однозначно больше
                return o1.getPosition().getBranch()>o2.getPosition().getBranch()?1:-1;
            }
            return 0;
    }
}
