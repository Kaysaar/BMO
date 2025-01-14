package kaysaar.bmo.buildingmenu;

import ashlib.data.plugins.ui.models.ImagePanel;
import ashlib.data.plugins.ui.models.PopUpUI;
import ashlib.data.plugins.ui.plugins.UILinesRenderer;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.IntervalUtil;
import kaysaar.bmo.buildingmenu.upgradepaths.CustomUpgradePath;
import kaysaar.bmo.buildingmenu.upgradepaths.UpgradePathManager;
import kaysaar.bmo.buildingmenu.popup.UpgradePathUI;
import org.lwjgl.input.Keyboard;

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
    IntervalUtil util = null;
    boolean expanded = false;
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

    public void createIndustryPanel(boolean isForUpgrade){
        subTooltip.setTitleOrbitronLarge();
        LabelAPI label = subTooltip.addTitle(currentSpec.getName());
        label.getPosition().inTL((mainPanel.getPosition().getWidth()/2)-(label.computeTextWidth(label.getText())/2),0);
        UILinesRenderer renderer = new UILinesRenderer(0f);
        ImagePanel panel = new ImagePanel();
        CustomPanelAPI panelHolder = holderPanel.createCustomPanel(mainPanel.getPosition().getWidth(),95,renderer);
        CustomPanelAPI panelImage  = panelHolder.createCustomPanel(190,95,panel);
        renderer.setPanel(panelImage);
        panel.init(panelImage,Global.getSettings().getSprite(currentSpec.getNewPluginInstance(market).getCurrentImage()));
        panelHolder.addComponent(panelImage).inTL(mainPanel.getPosition().getWidth()/2-95,0);

        subTooltip.addCustom(panelHolder,0f).getPosition().inTL(0,-label.getPosition().getY()+7);
        Industry.IndustryTooltipMode mode =Industry.IndustryTooltipMode.ADD_INDUSTRY;
        if(isForUpgrade){
            mode = Industry.IndustryTooltipMode.UPGRADE;

        }

        BuildingMenuMisc.createTooltipForIndustry((BaseIndustry) currentSpec.getNewPluginInstance(market),mode ,mainTooltip,expanded,false,mainPanel.getPosition().getWidth(),true,false,isForUpgrade);
        if(!BuildingMenuMisc.getIndustryTree(currentSpec.getId()).isEmpty()){
            buttonForUpgrade = buttonTooltip.addButton("Show industry upgrade path",null,market.getFaction().getBaseUIColor(),market.getFaction().getDarkUIColor(),Alignment.MID,CutStyle.ALL,mainPanel.getPosition().getWidth(),20,0f);
            buttonForUpgrade.getPosition().inTL(0,0);
        }
        holderPanel.addUIElement(mainTooltip).inTL(0,130);
        holderPanel.addUIElement(subTooltip).inTL(0,0);
        holderPanel.addUIElement(buttonTooltip).inTL(0,holderPanel.getPosition().getHeight()-leftSpace+135);
        mainPanel.addComponent(holderPanel).inTL(0,0);

    }
    public void recreateIndustryPanel(boolean isForUpgrade){
        mainPanel.removeComponent(holderPanel);
        holderPanel = Global.getSettings().createCustom(mainPanel.getPosition().getWidth(),mainPanel.getPosition().getHeight(),null);
        mainTooltip = holderPanel.createUIElement(mainPanel.getPosition().getWidth(),mainPanel.getPosition().getHeight()-leftSpace,true);
        subTooltip = holderPanel.createUIElement(mainPanel.getPosition().getWidth(),130,false);
        buttonTooltip = holderPanel.createUIElement(mainPanel.getPosition().getWidth(),leftSpace-10,false);

        createIndustryPanel(isForUpgrade);
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
        if(util!=null){
            util.advance(amount);
            if(util.intervalElapsed()){
                util = null;
            }
        }
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
        for (InputEventAPI event : events) {
            if(event.isConsumed())continue;
            if(event.getEventValue()== Keyboard.KEY_F1&&util==null){
                event.consume();
                if(currentSpec!=null){
                    util = new IntervalUtil(0.2f,0.2f);
                    expanded = !expanded;
                    recreateIndustryPanel(dialog.isInUpgradeMode());
                }

            }
        }
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
