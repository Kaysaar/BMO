package kaysaar.bmo.buildingmenu.upgradepaths;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import kaysaar.bmo.buildingmenu.BuildingMenuMisc;
import kaysaar.bmo.buildingmenu.IndustryItemPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IndustryItemsPanel implements CustomUIPanelPlugin {
    CustomPanelAPI mainPanel;

    public CustomPanelAPI getMainPanel() {
        return mainPanel;
    }
    public IndustryItemsPanel(float width, BaseIndustry ind ,boolean hovers) {
        float iconSize = 45;
        float seperator = 20f;
        float total = iconSize+seperator;
        Set<String> items = BuildingMenuMisc.getItemsForIndustry(ind.getSpec().getId(),hovers);
        float maxItemsPerRow = total*items.size()-seperator;
        int amountofRows = (int) Math.ceil(items.size()/maxItemsPerRow);
        if(amountofRows==0){
            amountofRows = 1;
        }
        float height = (iconSize+15)*amountofRows+(amountofRows-1)*seperator;
        mainPanel = Global.getSettings().createCustom(width,height,this);
        TooltipMakerAPI tooltip = mainPanel.createUIElement(width,height,false);
        float currItem = 0;
        float currRow = 0;
        for (String item : items) {

            float x = currItem*iconSize;
            float y = currRow*iconSize;
            if(currRow>0){
                y+=((currRow)*seperator);
            }
            if(currItem>0){
                x+=((currItem)*seperator);
            }
            tooltip.addCustom(new IndustryItemPanel(ind.getMarket(),ind.getSpec(),iconSize,item).getMainPanel(),0f).getPosition().inTL(x,y);
            currItem++;
            if(currItem>=maxItemsPerRow){
                currItem = 0;
                currRow++;
            }
        }
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
