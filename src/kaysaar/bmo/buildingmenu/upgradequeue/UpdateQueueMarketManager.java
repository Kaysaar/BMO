package kaysaar.bmo.buildingmenu.upgradequeue;

import com.fs.starfarer.api.Global;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class UpdateQueueMarketManager {
    ArrayList<UpdateQueueInstance>queues = new ArrayList<>();
    public void removeQueue(String id){
        for (UpdateQueueInstance queue : queues) {
            if(queue.containsItem(id)){
                queue.queue.clear();
                queues.remove(queue);
                break;
            }
        }
    }
    public void addNewQueue(Set<String> industries){
        LinkedHashMap<String,Float>map = new LinkedHashMap<>();
        for (String industry : industries) {
            map.put(industry, Global.getSettings().getIndustrySpec(industry).getCost());
        }
        UpdateQueueInstance instance = new UpdateQueueInstance(map);
        queues.add(instance);
    }
    public UpdateQueueInstance getQueue(String id){
        for (UpdateQueueInstance queue : queues) {
            if(queue.containsItem(id)){
                return queue;
            }
        }
        return null;
    }


}
