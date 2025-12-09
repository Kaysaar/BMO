package kaysaar.bmo.buildingmenu.additionalreq;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class AdditionalReqManager {
    //Note : this is used by aotd this is why there is only one instance of baseAdditionalReq use it ONLY for vanilla industries
    public static AdditionalReqManager instance;
    public LinkedHashSet<BaseAdditionalReq>reqLinkedHashMap = new LinkedHashSet<>();
    public static AdditionalReqManager getInstance() {
        if (instance == null) {
            instance = new AdditionalReqManager();

        }
        return instance;
    }
    public void  addReq(BaseAdditionalReq req){
        reqLinkedHashMap.add(req);
    }

    public LinkedHashSet<BaseAdditionalReq> getReqLinkedHashMap() {
        return reqLinkedHashMap;
    }
    public boolean doesMetReq(String industryId, MarketAPI market) {
        for (BaseAdditionalReq value : reqLinkedHashMap) {
            if(value.getIndustriesAffected().contains(industryId)){
                if(!value.metCriteria(market,industryId))return false;
            }
        }
        return true;
    }
    public String getAdditionalReasons(String industryId, MarketAPI market) {
        StringBuilder builder = new StringBuilder();
        boolean appended = false;
        for (BaseAdditionalReq value : reqLinkedHashMap) {
            if(value.getIndustriesAffected().contains(industryId)){
                if(!value.metCriteria(market,industryId))continue;
                if(appended){
                    builder.append("\n");
                }
                builder.append(value.getReason(market,industryId));
                appended = true;
            }
        }
        return builder.toString();
    }
}
