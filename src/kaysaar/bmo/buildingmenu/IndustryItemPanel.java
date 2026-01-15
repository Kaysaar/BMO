package kaysaar.bmo.buildingmenu;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.impl.items.BaseSpecialItemPlugin;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.econ.impl.InstallableItemEffect;
import com.fs.starfarer.api.impl.campaign.econ.impl.ItemEffectsRepo;
import com.fs.starfarer.api.impl.campaign.shared.WormholeManager;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;
import java.util.List;

public class IndustryItemPanel implements CustomUIPanelPlugin {
    public CustomPanelAPI mainPanel;

    public CustomPanelAPI getMainPanel() {
        return mainPanel;
    }

    public IndustryItemPanel(MarketAPI market, IndustrySpecAPI spec, float iconSize, String itemId) {
        this.mainPanel = Global.getSettings().createCustom(iconSize,iconSize+15,this);
        float width = iconSize;
        float height = iconSize+15;
        InstallableItemEffect effect = ItemEffectsRepo.ITEM_EFFECTS.get(itemId);
        TooltipMakerAPI tooltip = mainPanel.createUIElement(width,height,false);
        tooltip.addImage(Global.getSettings().getSpecialItemSpec(itemId).getIconName(),iconSize,iconSize,0f);
        tooltip.getPrev().getPosition().inTL(0,0);
        Industry ind =  spec.getNewPluginInstance(market);
        Color color ;
        final boolean metReq = effect.getUnmetRequirements(ind)==null||effect.getUnmetRequirements(ind).isEmpty();
        if(metReq) {
            color = Misc.getPositiveHighlightColor();
        }
        else{
            color = Misc.getNegativeHighlightColor();
        }
        ButtonAPI checkbox = tooltip.addAreaCheckbox("",null,color,color,color,10,10,0f);
        checkbox.getPosition().inTL(iconSize/2-5,iconSize+5);
        checkbox.setChecked(true);
        checkbox.setClickable(false);
        checkbox.setMouseOverSound(null);
        CargoAPI cargo = Global.getFactory().createCargo(false);
        // Edge-case fix regarding Wormhole Anchor item requiring data when showing tooltip
        CargoStackAPI stackAPI;
        if (itemId.equals("wormhole_anchor")) {
            WormholeManager.WormholeItemData itemData = new WormholeManager.WormholeItemData("standard", "unknown", "Unknown");
            stackAPI = Global.getFactory().createCargoStack(CargoAPI.CargoItemType.SPECIAL,new SpecialItemData(itemId,itemData.toJsonStr()),cargo);
        }
        else {
            stackAPI = Global.getFactory().createCargoStack(CargoAPI.CargoItemType.SPECIAL,new SpecialItemData(itemId,null),cargo);
        }
       final SpecialItemPlugin plugin = Global.getSettings().getSpecialItemSpec(itemId).getNewPluginInstance(stackAPI);
        tooltip.addTooltipTo(new TooltipMakerAPI.TooltipCreator() {
            @Override
            public boolean isTooltipExpandable(Object tooltipParam) {
                return false;
            }

            @Override
            public float getTooltipWidth(Object tooltipParam) {
                return 450;
            }

            @Override
            public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
                plugin.createTooltip(tooltip,expanded,null,null);
                if(metReq){
                    tooltip.addPara("This item can be installed in this market",Misc.getPositiveHighlightColor(),10f);
                }
                else{
                    tooltip.addPara("This item can't be installed in this market",Misc.getNegativeHighlightColor(),10f);

                }
            }
        },mainPanel, TooltipMakerAPI.TooltipLocation.RIGHT);
        mainPanel.addUIElement(tooltip).inTL(0,0);

    }

    @Override
    public void positionChanged(PositionAPI position) {

    }

    @Override
    public void renderBelow(float alphaMult) {

    }

    @Override
    public void render(float alphaMult) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void processInput(List<InputEventAPI> events) {

    }

    @Override
    public void buttonPressed(Object buttonId) {

    }
}
