package kaysaar.bmo.buildingmenu;

import ashlib.data.plugins.ui.models.ImagePanel;
import ashlib.data.plugins.ui.plugins.UILinesRenderer;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

import java.util.List;

import static kaysaar.bmo.buildingmenu.MarketDialog.isAvailableToBuild;

public class IndustryImageWithTitle implements CustomUIPanelPlugin {

    CustomPanelAPI mainPanel;
    String industryId;
    ButtonAPI buildButton;

    public IndustryImageWithTitle(String industryId, boolean doesHaveBuildButton, MarketAPI market) {
        this.industryId = industryId;
        UILinesRenderer renderer = new UILinesRenderer(0f);
        mainPanel = Global.getSettings().createCustom(190,110,renderer);
        IndustrySpecAPI spec = (Global.getSettings().getIndustrySpec(industryId));
        TooltipMakerAPI tooltip = mainPanel.createUIElement(200  ,110,false);
        Industry industry = spec.getNewPluginInstance(market);
        tooltip.addTitle(industry.getCurrentName());
        tooltip.getPrev().getPosition().inTL(0,0);
        ImagePanel panel = new ImagePanel();
        CustomPanelAPI panelImage  = mainPanel.createCustomPanel(190,95,panel);
        renderer.setPanel(panelImage);
        panel.init(panelImage,Global.getSettings().getSprite(industry.getCurrentImage()));
        tooltip.addCustom(panelImage,2f);
        if(doesHaveBuildButton){
           buildButton = tooltip.addButton("Queue",null,70,20,5f);
        }
        mainPanel.addUIElement(tooltip).inTL(0,0);

    }

    public String getIndustryId() {
        return industryId;
    }

    public CustomPanelAPI getMainPanel() {
        return mainPanel;
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

    public ButtonAPI getBuildButton() {
        return buildButton;
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
