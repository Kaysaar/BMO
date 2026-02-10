package kaysaar.bmo.buildingmenu.popup;

import ashlib.data.plugins.ui.models.PopUpUI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import kaysaar.bmo.buildingmenu.IndustryDropDownButton;
import kaysaar.bmo.buildingmenu.MarketDialog;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagManager;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagSpec;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagType;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TagFilterPopUp extends PopUpUI {
    MarketDialog marketDialog;
    CustomPanelAPI mainPanel;

    LinkedHashMap<String, IndustryTagSpec>tagFilterLinkedHashMap = new LinkedHashMap<>();
    ArrayList<ButtonAPI>buttons = new ArrayList<>();
    public TagFilterPopUp(MarketDialog dialog) {
        this.marketDialog = dialog;
        if(marketDialog.table.activeTags!=null){
            this.tagFilterLinkedHashMap.putAll(marketDialog.table.activeTags);
        }


    }

    @Override
    public void createUI(CustomPanelAPI panelAPI) {
        createUIMockup(panelAPI);
        panelAPI.addComponent(mainPanel).inTL(0, 0);
        addPanel(mainPanel);
    }

    @Override
    public float createUIMockup(CustomPanelAPI panelAPI) {
        mainPanel = panelAPI.createCustomPanel(panelAPI.getPosition().getWidth(), panelAPI.getPosition().getHeight(), null);
        TooltipMakerAPI tooltip = mainPanel.createUIElement(panelAPI.getPosition().getWidth() + 5, panelAPI.getPosition().getHeight(), true);
        tooltip.addSectionHeading("Generic Filters",marketDialog.market.getFaction().getBaseUIColor(),marketDialog.market.getFaction().getDarkUIColor(), Alignment.MID, 0f);
        ArrayList<IndustryDropDownButton> bt = marketDialog.table.getListConverted();
        for (IndustryTagSpec spec : IndustryTagManager.getTagsSpecBasedOnType(IndustryTagType.GENERIC)) {
            ArrayList<String>sp = spec.getSpecIdsForMatchup(marketDialog.market,bt);
            if (sp.isEmpty()) continue;
            createLabel(panelAPI, spec, sp, tooltip);

        }
        tooltip.addSectionHeading("Mods",marketDialog.market.getFaction().getBaseUIColor(),marketDialog.market.getFaction().getDarkUIColor(), Alignment.MID, 5f);
        for (IndustryTagSpec spec : IndustryTagManager.getTagsSpecBasedOnType(IndustryTagType.MOD)) {
            ArrayList<String>sp = spec.getSpecIdsForMatchup(marketDialog.market,bt);
            if (sp.isEmpty()) continue;
            createLabel(panelAPI, spec, sp, tooltip);

        }
//        tooltip.addSectionHeading("Other", Alignment.MID, 5f);
//        createLabel(panelAPI,"Showcase from which mod industries are in their tooltips",tooltip);
        tooltip.addSpacer(5f);
        mainPanel.getPosition().setSize(panelAPI.getPosition().getWidth(), tooltip.getHeightSoFar());
        mainPanel.addUIElement(tooltip).inTL(0, 0);
        float height =   tooltip.getHeightSoFar()+4;
        if(height>320){
            height = 320;
        }
        return height;


    }

    private  void createLabel(CustomPanelAPI panelAPI, IndustryTagSpec spec, ArrayList<String> sp, TooltipMakerAPI tooltip) {
        CustomPanelAPI panelToCreate = Global.getSettings().createCustom(panelAPI.getPosition().getWidth(), 20, null);
        TooltipMakerAPI tooltipButton = panelToCreate.createUIElement(panelAPI.getPosition().getWidth(), 20, false);
        ButtonAPI button  = tooltipButton.addCheckbox(20, 20, spec.tagName+" ("+ sp.size()+")", spec, Fonts.DEFAULT_SMALL, marketDialog.market.getFaction().getBaseUIColor(), ButtonAPI.UICheckboxSize.SMALL, 0f);
        if(tagFilterLinkedHashMap.get(spec.tag)!=null){
            button.setChecked(true);
        }
        buttons.add(button);

        panelToCreate.addUIElement(tooltipButton).inTL(0, 0);
        tooltip.addCustom(panelToCreate, 5f);
    }

    @Override
    public void onExit() {
        buttons.clear();
        marketDialog.table.activeTags.clear();
        marketDialog.table.activeTags.putAll(tagFilterLinkedHashMap);
        tagFilterLinkedHashMap.clear();
        marketDialog.util = new IntervalUtil(0.4f, 0.4f);
        marketDialog.table.recreateOldListBasedOnPrevSort();
        marketDialog.table.recreateTable();
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        for (ButtonAPI button : buttons) {
            IndustryTagSpec spec = (IndustryTagSpec) button.getCustomData();
            if(button.isChecked()&&!tagFilterLinkedHashMap.containsKey(spec.tag)){
                tagFilterLinkedHashMap.put(spec.tag,spec);
            }
            else if (!button.isChecked()){
                tagFilterLinkedHashMap.remove(spec.tag);
            }
        }
    }
}
