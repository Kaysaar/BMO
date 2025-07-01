package kaysaar.bmo.buildingmenu.popup;

import ashlib.data.plugins.misc.AshMisc;
import ashlib.data.plugins.ui.models.PopUpUI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.MutableValue;
import kaysaar.bmo.buildingmenu.BuildingMenuMisc;
import kaysaar.bmo.buildingmenu.IndustryImageWithTitle;
import kaysaar.bmo.buildingmenu.MarketDialog;
import kaysaar.bmo.buildingmenu.upgradepaths.CustomUpgradePath;
import kaysaar.bmo.buildingmenu.upgradequeue.UpdateQueueMainManager;
import kaysaar.bmo.buildingmenu.upgradequeue.UpdateQueueMarketManager;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static kaysaar.bmo.buildingmenu.BuildingMenuMisc.startQueue;
import static org.lwjgl.opengl.GL11.*;

public class UpgradePathUI extends PopUpUI {
    CustomUpgradePath upgradePath;
    float spaceBetweenColumns = 20f;
    float spacers = 5f;
    float spaceBetweenRows = 50f;
    float imagewidth = 195;
    float imageheight = 130;
    public float width, height;
    CustomPanelAPI mainPanel;
    MarketDialog marketDialog;
    TooltipMakerAPI tooltip;
    MarketAPI market;


    ArrayList<IndustryImageWithTitle> imageWithTitles = new ArrayList<>();

    public UpgradePathUI(CustomUpgradePath upgradePath, MarketDialog dialog) {
        this.upgradePath = upgradePath;
        this.marketDialog = dialog;
        width = upgradePath.columns * imagewidth + ((upgradePath.columns - 1) * spaceBetweenColumns + spacers * 2);
        height = upgradePath.rows * imageheight + ((upgradePath.rows - 1) * spaceBetweenRows + spacers * 3);
    }
    public UpgradePathUI(CustomUpgradePath upgradePath,MarketAPI market) {
        this.market= market;
        this.upgradePath = upgradePath;
        this.marketDialog = null;
        width = upgradePath.columns * imagewidth + ((upgradePath.columns - 1) * spaceBetweenColumns + spacers * 2);
        height = upgradePath.rows * imageheight + ((upgradePath.rows - 1) * spaceBetweenRows + spacers * 3);
    }
    @Override
    public void createUI(CustomPanelAPI panelAPI) {
        createUIMockup(panelAPI);
        panelAPI.addComponent(mainPanel).inTL(0, 0);
    }

    @Override
    public float createUIMockup(CustomPanelAPI panelAPI) {
        mainPanel = panelAPI.createCustomPanel(panelAPI.getPosition().getWidth(), panelAPI.getPosition().getHeight(), null);
        TooltipMakerAPI tooltip = mainPanel.createUIElement(panelAPI.getPosition().getWidth() , panelAPI.getPosition().getHeight(), true);
        boolean did = false;
        for (final Map.Entry<String, Vector2f> entry : upgradePath.getIndustryCoordinates().entrySet()) {
            IndustryImageWithTitle title = new IndustryImageWithTitle(entry.getKey(),did,market);
            did =true;
            tooltip.addCustom(title.getMainPanel(), 0f).getPosition().inTL(calculateX(entry.getValue().x), calculateY(entry.getValue().y));
            tooltip.addTooltipToPrevious(new TooltipMakerAPI.TooltipCreator() {
                @Override
                public boolean isTooltipExpandable(Object tooltipParam) {
                    return false;
                }

                @Override
                public float getTooltipWidth(Object tooltipParam) {
                    return 400f;
                }

                @Override
                public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
                    BaseIndustry ind = (BaseIndustry) Global.getSettings().getIndustrySpec(entry.getKey()).getNewPluginInstance(marketDialog.market);
                    BuildingMenuMisc.createTooltipForIndustry(ind, Industry.IndustryTooltipMode.ADD_INDUSTRY,tooltip,expanded,true,400,true,true,true);
                }
            }, TooltipMakerAPI.TooltipLocation.RIGHT,false);
            imageWithTitles.add(title);

        }
        tooltip.setHeightSoFar(height);
        mainPanel.addUIElement(tooltip).inTL(spacers/2, 0);

        return height;


    }

    public float calculateX(float index) {
        return (imagewidth + spaceBetweenColumns) * index + spacers;
    }

    public float calculateY(float index) {
        return (imageheight + spaceBetweenRows) * index + spacers;
    }

    @Override
    public void processInput(List<InputEventAPI> events) {
        if(marketDialog==null)return;
        super.processInput(events);
    }

    @Override
    public void renderBelow(float alphaMult) {
        super.renderBelow(alphaMult);
        boolean didIt = false;
        if(mainPanel!=null){
            AshMisc.startStencil(mainPanel,1f);
            for (IndustryImageWithTitle imageWithTitle : imageWithTitles) {
                IndustrySpecAPI spec = Global.getSettings().getIndustrySpec(imageWithTitle.getIndustryId());
                if (AshMisc.isStringValid(spec.getDowngrade())) {
                    IndustryImageWithTitle downgrade = getImageWithTitle(spec.getDowngrade());
                    drawTechLineVertical(imageWithTitle.getMainPanel().getPosition(),downgrade.getMainPanel().getPosition(), 0f);

                }
            }
            AshMisc.endStencil();
        }

    }

    public IndustryImageWithTitle getImageWithTitle(String industryId) {
        for (IndustryImageWithTitle imageWithTitle : imageWithTitles) {
            if (imageWithTitle.getIndustryId().equals(industryId)) {
                return imageWithTitle;
            }
        }
        return null;
    }

    void drawTechLineVertical(PositionAPI p1, PositionAPI p2, float correctorX) {
        glBegin(GL_LINE_LOOP);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

        if (p1.getCenterX() == p2.getCenterX()) {
            glVertex2f(p1.getCenterX(), p1.getCenterY());
            glVertex2f(p2.getCenterX(), p2.getCenterY());
            glEnd();
        }
        else{
            float distance = getExactYBottomFromPos(p2)-p2.getHeight() - getExactYBottomFromPos(p1);
            glVertex2f(p1.getCenterX(),p1.getCenterY());
            glVertex2f(p1.getCenterX(), getExactYBottomFromPos(p1)+p1.getHeight()+distance/2);
            glEnd();

            glBegin(GL_LINE_LOOP);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glVertex2f(p1.getCenterX(), getExactYBottomFromPos(p1)+p1.getHeight()+distance/2);
            glVertex2f(p2.getCenterX(), getExactYBottomFromPos(p1)+p1.getHeight()+distance/2);
            glEnd();

            glBegin(GL_LINE_LOOP);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            glVertex2f(p2.getCenterX(), getExactYBottomFromPos(p1)+p1.getHeight()+distance/2);
            glVertex2f(p2.getCenterX(), p2.getCenterY());
            glEnd();
        }
    }

    private  float getExactYBottomFromPos(PositionAPI p2) {
        return p2.getCenterY() - (p2.getHeight() / 2);
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        for (IndustryImageWithTitle imageWithTitle : imageWithTitles) {
            if(imageWithTitle.getBuildButton()!=null){
                if(imageWithTitle.getBuildButton().isChecked()){
                    imageWithTitle.getBuildButton().setChecked(false);
                    if(marketDialog!=null){
                        marketDialog.table.specToBuilt = Global.getSettings().getIndustrySpec(imageWithTitle.getIndustryId());
                        marketDialog.showcaseUI.setCurrentSpec(marketDialog.table.specToBuilt);
                        marketDialog.setInUpgradeMode(true);
                        marketDialog.showcaseUI.recreateIndustryPanel(true);
                        forceDismiss();
                    }
                    else{
                        startQueue(imageWithTitle.getIndustryId(),market);
                    }

                    return;
                }
            }
        }
    }



    @Override
    public void onExit() {
        super.onExit();
        imageWithTitles.clear();
        marketDialog.util = new IntervalUtil(0.4f, 0.4f);
    }
}
