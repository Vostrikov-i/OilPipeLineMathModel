package com.oilpipelinemodel.app.Interfaces.Wrappers;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ConnectedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ExchangedObject;
import com.oilpipelinemodel.app.Utils.PairPosition;

public interface Wrappers extends ConnectedObject {
    ExchangedObject getExchangedInstance();
    PairPosition getPosition();
    ExchangedObject getLeftObject();
    ExchangedObject getRightObject();
}
