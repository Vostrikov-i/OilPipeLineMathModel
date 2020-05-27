package com.oilpipelinemodel.app.Repository.Manager;

import com.oilpipelinemodel.app.Interfaces.Primitives.CalculatedObject;
import com.oilpipelinemodel.app.Interfaces.Repositoryes.ManagedRepository;
import com.oilpipelinemodel.app.Interfaces.Repositoryes.MediatorModelRepo;
import com.oilpipelinemodel.app.Interfaces.Wrappers.Wrappers;
import com.oilpipelinemodel.app.Utils.PairPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* Класс менеджера репозиториев
*
*
* */
public class PipeObjectsRepoManager implements ManagerRepository, MediatorModelRepo<PairPosition, CalculatedObject> {
   private final Map<PairPosition , Wrappers> mapElements= new HashMap<>();

   //метод синхронизирован
    //TODO рассмотреть ConcurencyHashMap а метод обновления ссылок вынести в private и сделать syncronized, пока будет так
    @Override
   public synchronized void addElement(Wrappers object){
       mapElements.put(object.getPosition(), object);
       // после добавления надо обновить ссылки у элементов
       List<Wrappers> listLinkElements=mapElements.entrySet().stream()
               .filter(x->(x.getValue().getPosition().getBranch()==object.getPosition().getBranch())) // только те которые на одной ветке
               .filter(x->Math.abs((x.getValue().getPosition().getBranchPosition()-object.getPosition().getBranchPosition()))==1) // только соседние позиции
               .map(Map.Entry::getValue)
               .collect(Collectors.toList());

       //TODO возможно поменять на лямбды
       for(Wrappers wrapper: listLinkElements){
            if ((wrapper.getPosition().getBranchPosition()+1)==object.getPosition().getBranchPosition()){ //предыдущий элемент на ветке
                wrapper.setRightObject(object.getExchangedInstance()); //добавляемый элемент становится правым объектом
                object.setLeftObject(wrapper.getExchangedInstance()); // назначаем левый объект у добавляемого элемента
            }
           if ((wrapper.getPosition().getBranchPosition()-1)==object.getPosition().getBranchPosition()){ // следующий элемент на ветке
               wrapper.setLeftObject(object.getExchangedInstance()); // добавляемый элемент становится левым объектом
               object.setRightObject(wrapper.getExchangedInstance()); // назначаем правый объект у добавляемого элемента
           }
       }

   }
    @Override
   public void deleteElement(Wrappers object){
       mapElements.values().removeIf(x->x.equals(object)); //remove by value
   }

    @Override
    public Map<PairPosition, CalculatedObject> getObjectsMap() {
        return new HashMap<>(mapElements); //вернули карту объектов
    }

    //приватный метод для расставления связей между объектами

}
