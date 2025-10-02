package kaysaar.bmo;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import kaysaar.bmo.listeners.BuildingMenuListener;

import java.util.HashSet;

public class BmoListener {
    public static HashSet<String> aboutToCreateBuildingMenu(MarketAPI market){
        HashSet<String> aboutToCreateBuildingMenu = new HashSet<>();
        Global.getSector().getListenerManager().getListeners(BuildingMenuListener.class).forEach(x->{
            aboutToCreateBuildingMenu.addAll(x.addBuildingsToBeHidden(market));
        });
        return aboutToCreateBuildingMenu;
    }
}
