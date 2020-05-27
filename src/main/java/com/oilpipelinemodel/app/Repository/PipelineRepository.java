package com.oilpipelinemodel.app.Repository;

import com.oilpipelinemodel.app.Interfaces.Repositoryes.SimpleRepository;
import com.oilpipelinemodel.app.Repository.Manager.ManagerRepository;
import com.oilpipelinemodel.app.Utils.PairPosition;
import com.oilpipelinemodel.app.Wrappers.PipelineWrapper;

import java.util.HashMap;

public class PipelineRepository implements SimpleRepository<PipelineWrapper> {

    private ManagerRepository manager;

    public PipelineRepository(ManagerRepository manager){
        this.manager=manager;
    }

    @Override
    public void add(PipelineWrapper element) {
        manager.addElement(element);

    }

    @Override
    public void delete(PipelineWrapper element) {
        manager.deleteElement(element);
    }
}
