package com.oilpipelinemodel.app.Interfaces.Repositoryes;

import com.oilpipelinemodel.app.Interfaces.Wrappers.Wrappers;

/*Интерфейс репозитория для добавления и удаления объектов
*
*
*
* */
public interface SimpleRepository<T extends Wrappers> {
    void add(T element);
    void delete(T element);
}
