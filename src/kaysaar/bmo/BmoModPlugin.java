package kaysaar.bmo;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import kaysaar.bmo.buildingmenu.BuildingUITracker;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagManager;

import static ashlib.data.plugins.AshLibPlugin.fontInsigniaMedium;

public class BmoModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();

        // Test that the .jar is loaded and working, using the most obnoxious way possible.
        Global.getSettings().loadFont(fontInsigniaMedium);
        IndustryTagManager.loadDefaultTags();
        IndustryTagManager.loadModdedTags();
    }

    @Override
    public void onGameLoad(boolean newGame) {
        Global.getSector().addTransientScript(new BuildingUITracker());
    }

    // You can add more methods from ModPlugin here. Press Control-O in IntelliJ to see options.
}
