package kaysaar.bmo.buildingmenu.additionalreq;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;

import java.util.ArrayList;

public class BaseAdditionalReq {
    public boolean metCriteria(MarketAPI market, String indId){
        return true;
    }
    public String getReason(MarketAPI market, String indId){
        return "";
    }
    public ArrayList<String> getIndustriesAffected(){
        return new ArrayList<>();
    }

}
