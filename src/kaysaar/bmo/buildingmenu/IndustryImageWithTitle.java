package kaysaar.bmo.buildingmenu;

import ashlib.data.plugins.ui.models.ImagePanel;
import ashlib.data.plugins.ui.plugins.UILinesRenderer;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

import java.util.List;

public class IndustryImageWithTitle implements CustomUIPanelPlugin {

    CustomPanelAPI mainPanel;
    String industryId;
    public IndustryImageWithTitle(String industryId) {
        this.industryId = industryId;
        UILinesRenderer renderer = new UILinesRenderer(0f);
        mainPanel = Global.getSettings().createCustom(190,110,renderer);
        TooltipMakerAPI tooltip = mainPanel.createUIElement(200  ,110,false);
        tooltip.addTitle(Global.getSettings().getIndustrySpec(industryId).getName());
        tooltip.getPrev().getPosition().inTL(0,0);
        ImagePanel panel = new ImagePanel();
        CustomPanelAPI panelImage  = mainPanel.createCustomPanel(190,95,panel);
        renderer.setPanel(panelImage);
        panel.init(panelImage,Global.getSettings().getSprite(Global.getSettings().getIndustrySpec(industryId).getImageName()));
        tooltip.addCustom(panelImage,2f);
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
