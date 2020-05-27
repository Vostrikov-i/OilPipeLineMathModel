package com.oilpipelinemodel.app.Primitives;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

enum DataOrder {
    PRESSURE(0),
    VELOCITY(1),
    POSITIVE_PRESSURE(2),
    NEGATIVE_PRESSURE(3),
    POSITIVE_VELOCITY(4),
    NEGATIVE_VELOCITY(5),
    ;

    private int index;

    DataOrder(int index)
    {
        this.index=index;
    }
    public int getIndex() {
        return index;
    }
    public static int getMaxIndex(){
        DataOrder[] dataOrders=DataOrder.values();
        Optional<DataOrder> max= Arrays.stream(dataOrders).max(Comparator.comparing(DataOrder::getIndex));
        DataOrder currObj=max.get();
        return (currObj.getIndex()+1);
    }
}
