package com.oilpipelinemodel.app.Utils;

import java.util.Objects;

final public class ParametersSaver<T> {
    private T parameter;
    private String parameterDescription;


    public ParametersSaver(String parameterDescription, T initialValue){
        this.parameterDescription=parameterDescription;
        this.parameter=initialValue;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public String getParameterDescription() {
        return parameterDescription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametersSaver<?> that = (ParametersSaver<?>) o;
        return Objects.equals(parameter, that.parameter) &&
                Objects.equals(parameterDescription, that.parameterDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameter, parameterDescription);
    }

    @Override
    public String toString() {
        return "ParametersSaver{" +
                "parameter=" + parameter +
                ", parameterDescription='" + parameterDescription + '\'' +
                '}';
    }
}
