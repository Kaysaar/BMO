package kaysaar.bmo.buildingmenu.upgradepaths;

import org.lwjgl.util.vector.Vector2f;

import java.util.LinkedHashMap;

public class CustomUpgradePath {
    public int columns;
    public int rows;
    //Coordinates x -> column y -> row
    LinkedHashMap<String, Vector2f>industryCoordinates;
    public CustomUpgradePath(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;

    }
    public LinkedHashMap<String, Vector2f> getIndustryCoordinates() {
        return industryCoordinates;
    }
    public void setIndustryCoordinates(LinkedHashMap<String, Vector2f> industryCoordinates) {
        this.industryCoordinates = industryCoordinates;
    }

}
