package com.oilpipelinemodel.app.Repository.Manager;

import com.oilpipelinemodel.app.Interfaces.Wrappers.Wrappers;

//интерфейс менеджера репозиториев
public interface ManagerRepository {
    void addElement(Wrappers wrappers);
    void deleteElement(Wrappers wrappers);
}
