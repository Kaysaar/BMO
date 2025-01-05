package kaysaar.bmo;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import kaysaar.bmo.buildingmenu.BuildingUITracker;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagManager;
import kaysaar.bmo.buildingmenu.upgradepaths.CustomUpgradePath;
import kaysaar.bmo.buildingmenu.upgradepaths.UpgradePathManager;
import org.lwjgl.util.vector.Vector2f;

import java.util.LinkedHashMap;

import static ashlib.data.plugins.AshLibPlugin.fontInsigniaMedium;

public class BmoModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();

        // Test that the .jar is loaded and working, using the most obnoxious way possible.
        Global.getSettings().loadFont(fontInsigniaMedium);

    }

    @Override
    public void onGameLoad(boolean newGame) {
        Global.getSector().addTransientScript(new BuildingUITracker());
        //Note : Should be done from AoTD side, but i dont wanna make sudden update
        if(Global.getSettings().getModManager().isModEnabled("aotd_vok")){
            CustomUpgradePath path = new CustomUpgradePath(3,3);
            LinkedHashMap<String, Vector2f>map = new LinkedHashMap<>();
            map.put("extractive", new Vector2f(1,0));
            map.put("mining", new Vector2f(1,1));
            map.put("fracking", new Vector2f(0,2));
            map.put("mining_megaplex", new Vector2f(2,2));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"extractive");

            path = new CustomUpgradePath(3,4);
            map = new LinkedHashMap<>();
            map.put("smelting", new Vector2f(1,0));
            map.put("refining", new Vector2f(1,1));
            map.put("crystalizator", new Vector2f(0,2));
            map.put("isotope_separator", new Vector2f(2,2));
            map.put("policrystalizator", new Vector2f(0,3));
            map.put("cascade_reprocesor", new Vector2f(2,3));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"smelting");

            path = new CustomUpgradePath(3,3);
            map = new LinkedHashMap<>();
            map.put("lightproduction", new Vector2f(1,0));
            map.put("lightindustry", new Vector2f(1,1));
            map.put("hightech", new Vector2f(0,2));
            map.put("druglight", new Vector2f(1,2));
            map.put("consumerindustry", new Vector2f(2,2));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"lightproduction");

            path = new CustomUpgradePath(3,3);
            map = new LinkedHashMap<>();
            map.put("monoculture", new Vector2f(1,0));
            map.put("farming", new Vector2f(1,1));
            map.put("artifarming", new Vector2f(0,2));
            map.put("subfarming", new Vector2f(2,2));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"monoculture");

            path = new CustomUpgradePath(1,2);
            map = new LinkedHashMap<>();
            map.put("fuelprod", new Vector2f(0,0));
            map.put("blast_processing", new Vector2f(0,1));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"fuelprod");

            path = new CustomUpgradePath(1,2);
            map = new LinkedHashMap<>();
            map.put("waystation", new Vector2f(0,0));
            map.put("terminus", new Vector2f(0,1));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"waystation");

            path = new CustomUpgradePath(1,2);
            map = new LinkedHashMap<>();
            map.put("fishery", new Vector2f(0,0));
            map.put("aquaculture", new Vector2f(0,1));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"fishery");

            path = new CustomUpgradePath(4,5);
            map = new LinkedHashMap<>();
            map.put("shityheavy", new Vector2f(2,0));
            map.put("heavyindustry", new Vector2f(2,1));
            map.put("orbitalworks", new Vector2f(1,2));
            map.put("supplyheavy", new Vector2f(3,2));
            map.put("orbitalheavy",new Vector2f(1,3));
            map.put("triheavy",new Vector2f(2,4));
            map.put("hegeheavy",new Vector2f(0,4));
            map.put("stella_manufactorium",new Vector2f(3,4));
            path.setIndustryCoordinates(map);
            UpgradePathManager.getInstance().addNewCustomPath(path,"shityheavy");

        }
    }

    // You can add more methods from ModPlugin here. Press Control-O in IntelliJ to see options.
}
