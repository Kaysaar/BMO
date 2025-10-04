package kaysaar.bmo.buildingmenu.upgradepaths;

import ashlib.data.plugins.misc.AshMisc;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import kaysaar.bmo.buildingmenu.BuildingMenuMisc;
import org.lwjgl.util.vector.Vector2f;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

public class UpgradePathManager {

    public static UpgradePathManager mainInstance;
    LinkedHashMap<String,CustomUpgradePath> genericUpgradePaths;
    //This is here mainly for Ashes of The Domain cause it is only mod that introduces framework for non-linear upgrade paths
    LinkedHashMap<String,CustomUpgradePath> customUpgradePaths;
    public static UpgradePathManager getInstance(){
        if(mainInstance == null){
            mainInstance = new UpgradePathManager();
            mainInstance.genericUpgradePaths = new LinkedHashMap<>();
            mainInstance.customUpgradePaths = new LinkedHashMap<>();
        }
        return mainInstance;
    }
    public void addNewUpgradePath(CustomUpgradePath customUpgradePath,String industryId){
        genericUpgradePaths.put(industryId,customUpgradePath);
    }
    public void addNewCustomPath(CustomUpgradePath customUpgradePath,String industryId){
        customUpgradePaths.put(industryId,customUpgradePath);
    }
    public CustomUpgradePath getCustomUpgradePath(String industryId){
        //First we do prioritize custom ones
        if(customUpgradePaths.containsKey(industryId)){
            return customUpgradePaths.get(industryId);
        }
        // we have not found main one time to look for others
        for (CustomUpgradePath value : customUpgradePaths.values()) {
            if(value.getIndustryCoordinates().containsKey(industryId)){
                return value;
            }
        }
        return genericUpgradePaths.get(industryId);
    }
    public void rePopulate(){
        for (IndustrySpecAPI industrySpecAPI : BuildingMenuMisc.getAllSpecsWithoutDowngradeAndItsSubItems()) {
            if(AshMisc.isStringValid(industrySpecAPI.getUpgrade())){
                LinkedHashSet<String>ids = new LinkedHashSet<>();
                IndustrySpecAPI curr = industrySpecAPI;
                ids.add(curr.getId());
                while (AshMisc.isStringValid(curr.getUpgrade())){
                    curr = Global.getSettings().getIndustrySpec(curr.getUpgrade());
                    ids.add(curr.getId());
                }
                CustomUpgradePath path = new CustomUpgradePath(1,ids.size());
                LinkedHashMap<String, Vector2f>vectors = new LinkedHashMap<>();
                int row =0;
                for (String id : ids) {
                    vectors.put(id, new Vector2f(0,row));
                    row++;
                }
                path.setIndustryCoordinates(vectors);
                addNewUpgradePath(path,industrySpecAPI.getId());
            }
        }
    }


}
