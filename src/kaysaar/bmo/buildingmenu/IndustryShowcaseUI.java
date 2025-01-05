package kaysaar.bmo.buildingmenu;

import ashlib.data.plugins.ui.models.ImagePanel;
import ashlib.data.plugins.ui.models.PopUpUI;
import ashlib.data.plugins.ui.plugins.UILinesRenderer;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import kaysaar.bmo.buildingmenu.upgradepaths.CustomUpgradePath;
import kaysaar.bmo.buildingmenu.upgradepaths.UpgradePathManager;
import kaysaar.bmo.buildingmenu.popup.UpgradePathUI;

import java.util.List;

public class IndustryShowcaseUI implements CustomUIPanelPlugin {

    int leftSpace =170;
    CustomPanelAPI mainPanel;
    CustomPanelAPI holderPanel;
    TooltipMakerAPI mainTooltip;
    TooltipMakerAPI subTooltip;
    TooltipMakerAPI buttonTooltip;
    ButtonAPI buttonForUpgrade;
    IndustrySpecAPI currentSpec;
    MarketAPI market;
    MarketDialog dialog;
    public CustomPanelAPI getMainPanel() {
        return mainPanel;
    }

    public IndustryShowcaseUI(float width, float height,MarketDialog dialog) {
        this.market = dialog.market;
        this.dialog = dialog;
        mainPanel = Global.getSettings().createCustom(width,height,this);

        holderPanel = Global.getSettings().createCustom(width,height,null);
    }

    public void setCurrentSpec(IndustrySpecAPI currentSpec) {
        this.currentSpec = currentSpec;
    }

    public void createIndustryPanel(){
        subTooltip.setTitleOrbitronLarge();
        subTooltip.addTitle(currentSpec.getName());
        UILinesRenderer renderer = new UILinesRenderer(0f);
        ImagePanel panel = new ImagePanel();
        CustomPanelAPI panelHolder = holderPanel.createCustomPanel(mainPanel.getPosition().getWidth(),95,renderer);
        CustomPanelAPI panelImage  = panelHolder.createCustomPanel(190,95,panel);
        renderer.setPanel(panelImage);
        panel.init(panelImage,Global.getSettings().getSprite(currentSpec.getImageName()));
        panelHolder.addComponent(panelImage).inTL(mainPanel.getPosition().getWidth()/2-95,0);

        subTooltip.addCustom(panelHolder,7f);
        BuildingMenuMisc.createTooltipForIndustry((BaseIndustry) currentSpec.getNewPluginInstance(market), Industry.IndustryTooltipMode.ADD_INDUSTRY,mainTooltip,true,false,mainPanel.getPosition().getWidth(),true);
        if(!BuildingMenuMisc.getIndustryTree(currentSpec.getId()).isEmpty()){
            buttonForUpgrade = buttonTooltip.addButton("Show industry upgrade path",null,mainPanel.getPosition().getWidth(),20,0f);
            buttonForUpgrade.getPosition().inTL(0,0);
        }
        holderPanel.addUIElement(mainTooltip).inTL(0,130);
        holderPanel.addUIElement(subTooltip).inTL(0,0);
        holderPanel.addUIElement(buttonTooltip).inTL(0,holderPanel.getPosition().getHeight()-leftSpace+135);
        mainPanel.addComponent(holderPanel).inTL(0,0);

    }
    public void recreateIndustryPanel(){
        mainPanel.removeComponent(holderPanel);
        holderPanel = Global.getSettings().createCustom(mainPanel.getPosition().getWidth(),mainPanel.getPosition().getHeight(),null);
        mainTooltip = holderPanel.createUIElement(mainPanel.getPosition().getWidth(),mainPanel.getPosition().getHeight()-leftSpace,true);
        subTooltip = holderPanel.createUIElement(mainPanel.getPosition().getWidth(),130,false);
        buttonTooltip = holderPanel.createUIElement(mainPanel.getPosition().getWidth(),leftSpace-10,false);

        createIndustryPanel();
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
        if(buttonForUpgrade!=null&&buttonForUpgrade.isChecked()){
            buttonForUpgrade.setChecked(false);
//            CustomUpgradePath path = new CustomUpgradePath(3,3);
//            LinkedHashMap<String, Vector2f>map = new LinkedHashMap<>();
//            map.put("extractive", new Vector2f(1,0));
//            map.put("mining", new Vector2f(1,1));
//            map.put("fracking", new Vector2f(0,2));
//            map.put("mining_megaplex", new Vector2f(2,2));
//            path.setIndustryCoordinates(map);
            CustomUpgradePath path = UpgradePathManager.getInstance().getCustomUpgradePath(currentSpec.getId());
            if(path!=null){
                UpgradePathUI ui = new UpgradePathUI(path,dialog);
                dialog.dissableExit = true;
                float height = Global.getSettings().getScreenHeight();
                if(ui.height <=height){
                    height = ui.height;
                }
                placePopUpUI(ui, buttonForUpgrade,ui.width, height);
            }

        }
    }

    @Override
    public void processInput(List<InputEventAPI> events) {

    }

    @Override
    public void buttonPressed(Object buttonId) {

    }
    public static void placePopUpUI(PopUpUI ui, ButtonAPI button, float initWidth, float initHeight) {
        float width1 = initWidth;
        float height1 = initHeight;
        CustomPanelAPI panelAPI = Global.getSettings().createCustom(width1, height1, ui);
        float x = button.getPosition().getX() - width1;
        float y = button.getPosition().getY() + button.getPosition().getHeight();
        if (x <0) {
            x =0;
        }

        if (y - height1 <= 0.0F) {
            y = height1;
        }

        if (y > Global.getSettings().getScreenHeight()) {
            y = Global.getSettings().getScreenHeight() - 10.0F;
        }

        ui.init(panelAPI, x, y, false);
    }

}
