package kaysaar.bmo.listeners;

import com.fs.starfarer.api.campaign.econ.MarketAPI;

import java.util.HashSet;

public interface BuildingMenuListener {
    public HashSet<String>addBuildingsToBeHidden(MarketAPI market);

}
