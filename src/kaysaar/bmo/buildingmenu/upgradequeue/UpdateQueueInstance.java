package kaysaar.bmo.buildingmenu.upgradequeue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class UpdateQueueInstance {
    public LinkedHashMap<String,Float>queueData;
    public ArrayList<String>queue;
    public LinkedHashMap<String,Float>queueCopy;
    public String currIdOfUpgrade =null;
    public String lastId = currIdOfUpgrade=null;
    float cost = 0f;
    public  int currIndexOfUpgrade = -1;
    public UpdateQueueInstance(LinkedHashMap<String,Float> queue) {
        this.queue = new ArrayList<>();
        this.queue.addAll(queue.keySet());
        this.queueData = queue;
        queueCopy = new LinkedHashMap<>(queue);
        popFromTop();
    }
    public String getCurrIdOfUpgrade(){
        return currIdOfUpgrade;

    }

    public String getLastId() {
        return lastId;
    }

    public float getTotalCostForReturn(){
        float currCost = cost*0.4f;
        for (Float value : queueData.values()) {
            currCost+=value;
        }
        return currCost;
    }
    public float getTotalCostForReturnIgnoreCurrent(){
        float currCost = 0f;
        for (Float value : queueData.values()) {
            currCost+=value;
        }
        return currCost;
    }
    public float getTotalCostForReturnIgnoringCut(){
        float currCost = cost;
        for (Float value : queueData.values()) {
            currCost+=value;
        }
        return currCost;
    }
    public String getNextInLine(){
        currIndexOfUpgrade++;
        if(currIndexOfUpgrade >= queue.size()){
            currIndexOfUpgrade--;
            return null;
        }
        String res = queue.get(currIndexOfUpgrade);
        currIndexOfUpgrade--;
        return res;
    }
    public String getNextPrevInLine(){
        if(currIdOfUpgrade==null){
            return "";
        }
        return currIdOfUpgrade;
    }
    public  void clear(){
        queueData.clear();
        queueCopy.clear();


    }
    public void popFromTop(){
        if(queueData.isEmpty()){
            currIdOfUpgrade = null;
            return;
        }
        lastId = currIdOfUpgrade;
        currIndexOfUpgrade ++;
        if(currIndexOfUpgrade>=queue.size()){
            currIndexOfUpgrade =queue.size()-1;
        }
        currIdOfUpgrade = queue.get(currIndexOfUpgrade);
        if(lastId==null){
            lastId = currIdOfUpgrade;
        }
        queueData.remove(currIdOfUpgrade);

    }
    public boolean containsItem (String id){
        return  queueCopy.containsKey(id);
    }

}
