package com.oilpipelinemodel.app.Wrappers;
/*
* Обертка для нефтепровода
* Нужна для того чтобы
* 1. Хранить PairPosition - позицию объекта на нефтепроводе
* 2. Избавиться от лишних методов getFirstPoint() , getLastPoint() в сигнатуре класса
* 3. Предоставить конечному пользователю уровень абстракции чтобы он мог делать свои обертки
* */

import com.oilpipelinemodel.app.Interfaces.Primitives.ConnectedObject;
import com.oilpipelinemodel.app.Interfaces.Primitives.ExchangedObject;
import com.oilpipelinemodel.app.Interfaces.Wrappers.Wrappers;
import com.oilpipelinemodel.app.Primitives.Pipeline;
import com.oilpipelinemodel.app.Utils.PairPosition;
import org.jetbrains.annotations.Nullable;


final public class PipelineWrapper implements Wrappers {

    private Pipeline pipeline;
    private PairPosition position;

    @Nullable
    private ExchangedObject leftObject=null;
    @Nullable
    private ExchangedObject rightObject=null;

    public PipelineWrapper(PairPosition position, double diameter, long length) {
        pipeline = new Pipeline(diameter, length);
        this.position = position;
    }

    public void setPosition(PairPosition position) {
        this.position = position;
    }

    @Override
    public void setLeftObject(ExchangedObject exchangedObject) {
        this.leftObject=exchangedObject;
        pipeline.setLeftObject(exchangedObject);
    }

    @Override
    public void setRightObject(ExchangedObject exchangedObject) {
        this.rightObject=exchangedObject;
        pipeline.setRightObject(exchangedObject);
    }

    @Override
    public void calcParameters() {
        pipeline.calcParameters();
    }


    @Override
    public ExchangedObject getExchangedInstance() {
        return pipeline;
    }

    @Override
    public PairPosition getPosition() {
        return PairPosition.newInstance(position.getBranch(), position.getBranchPosition());
    }

    @Override
    @Nullable
    /*
     * Потенциально опасная операция
     * Left и Right Object мы не можем сделать final, т.к при добавлении/удалении нового элемента
     * придется пересоздавать заново всю карту трубопровода
     * TODO оценить риски данного решения, возможно пересмотреть архитектуру
     *
     * */
    public ExchangedObject getLeftObject() {
        return leftObject;
    }

    @Override
    @Nullable
    public ExchangedObject getRightObject() {
        return rightObject;
    }

    @Override
    public String toString() {
        return "PipelineWrapper{" +
                "pipeline=" + pipeline +
                ", position=" + position +
                ", leftObject=" + leftObject +
                ", rightObject=" + rightObject +
                '}';
    }
}
