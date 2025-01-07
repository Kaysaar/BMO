package kaysaar.bmo.buildingmenu.tooltipinjector;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.listeners.BaseIndustryOptionProvider;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagManager;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagSpec;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagType;

import java.awt.*;

public class ModIndustryTooltipInjector extends BaseIndustryOptionProvider {
    @Override
    public void addToIndustryTooltip(Industry ind, Industry.IndustryTooltipMode mode, TooltipMakerAPI tooltip, float width, boolean expanded) {
        super.addToIndustryTooltip(ind, mode, tooltip, width, expanded);
        if (IndustryTagManager.getTagsSpecBasedOnType(IndustryTagType.MOD).isEmpty()) {
            IndustryTagManager.loadModdedTags();
            IndustryTagManager.loadDefaultTags();
        }
        for (IndustryTagSpec spec : IndustryTagManager.getTagsSpecBasedOnType(IndustryTagType.MOD)) {
            if (spec.specs.contains(ind.getSpec().getId()) && !IndustryTagManager.vanillaIndustries.contains(ind.getSpec().getId())) {
                tooltip.addSectionHeading("Mod", Alignment.MID, 5f);
                tooltip.addPara("This industry is from %s", 5f, Color.ORANGE, spec.tagName);
                tooltip.addSectionHeading("", Alignment.MID, 5f);
            }
        }
    }

}
