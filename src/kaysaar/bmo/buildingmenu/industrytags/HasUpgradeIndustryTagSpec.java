package kaysaar.bmo.buildingmenu.industrytags;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import kaysaar.bmo.buildingmenu.BuildingMenuMisc;
import kaysaar.bmo.buildingmenu.IndustryDropDownButton;
import kaysaar.bmo.buildingmenu.MarketDialog;

import java.util.ArrayList;

public class HasUpgradeIndustryTagSpec extends IndustryTagSpec{
    public HasUpgradeIndustryTagSpec(String tag, String tagName, ArrayList<String> specsManuallyPlaced, IndustryTagType type) {
        super(tag, tagName, specsManuallyPlaced, type);
    }
    @Override
    public ArrayList<String> getSpecIdsForMatchup(MarketAPI market, ArrayList<IndustryDropDownButton> buttonsExisting) {
        ArrayList<String>toReturn = new ArrayList<>();
        for (IndustryDropDownButton industryDropDownButton : buttonsExisting) {
            for (IndustrySpecAPI o : industryDropDownButton.getSpecs()) {
                if(specs.contains(o.getId())){
                    Industry ind = o.getNewPluginInstance(market);
                    if(!BuildingMenuMisc.getIndustryTree(o.getId()).isEmpty()){
                        toReturn.add(ind.getId());
                    }
                }
            }
        }
        return  toReturn;
    }
}
