package spazley.scalingguis.config;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class CustomScales {
    public int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
    public int hudScale = Minecraft.getMinecraft().gameSettings.guiScale;
    public int tooltipScale = Minecraft.getMinecraft().gameSettings.guiScale;
    public JsonObject customIndividualGUIScales = new JsonObject();
    public JsonObject customGroupGUIScales = new JsonObject();
    public SortedSet<String> loggedGUIClassNames = new TreeSet<>();


    public void checkCustomEntries()
    {
        for (String s : JsonHelper.getKeyList(customIndividualGUIScales)) {
            if (!customIndividualGUIScales.getAsJsonObject(s).has("scale")) {
                customIndividualGUIScales.getAsJsonObject(s).addProperty("scale", Minecraft.getMinecraft().gameSettings.guiScale);
            }
            if (!customIndividualGUIScales.getAsJsonObject(s).has("name")) {
                customIndividualGUIScales.getAsJsonObject(s).addProperty("name", s);
            }
        }
        for (String s : JsonHelper.getKeyList(customGroupGUIScales)) {
            if (!customGroupGUIScales.getAsJsonObject(s).has("scale")) {
                customGroupGUIScales.getAsJsonObject(s).addProperty("scale", Minecraft.getMinecraft().gameSettings.guiScale);
            }
            if (!customGroupGUIScales.getAsJsonObject(s).has("name")) {
                customGroupGUIScales.getAsJsonObject(s).addProperty("name", s);
            }
        }
    }

/*
    public List<IConfigElement> getMainsList()
    {
        List<IConfigElement> list = new ArrayList<>();
        int defaultScale = Minecraft.getMinecraft().gameSettings.guiScale;
        int minValue = 0;
        int maxValue = 3;

        Property guiScaleProp = new Property("guiScale", String.valueOf(guiScale), Property.Type.INTEGER, "scalingguis.config.main.guiscale");
        guiScaleProp.setDefaultValue(defaultScale);
        guiScaleProp.setMinValue(minValue);
        guiScaleProp.setMaxValue(maxValue);
        list.add(new ScaleConfigElement(guiScaleProp).setCustomListEntryClass(SnappingSliderEntry.class));

        Property hudScaleProp = new Property("hudScale", String.valueOf(hudScale), Property.Type.INTEGER, "scalingguis.config.main.hudscale");
        hudScaleProp.setDefaultValue(defaultScale);
        hudScaleProp.setMinValue(minValue);
        hudScaleProp.setMaxValue(maxValue);
        list.add(new ScaleConfigElement(hudScaleProp).setCustomListEntryClass(SnappingSliderEntry.class));

        Property tooltipScaleProp = new Property("tooltipScale", String.valueOf(tooltipScale), Property.Type.INTEGER, "scalingguis.config.main.tooltipscale");
        tooltipScaleProp.setDefaultValue(defaultScale);
        tooltipScaleProp.setMinValue(minValue);
        tooltipScaleProp.setMaxValue(maxValue);
        list.add(new ScaleConfigElement(tooltipScaleProp).setCustomListEntryClass(SnappingSliderEntry.class));

        return list;
    }
*/

/*
    public List<IConfigElement> getIndividualsList()
    {
        List<IConfigElement> list = new ArrayList<>();
        int defaultScale = Minecraft.getMinecraft().gameSettings.guiScale;
        int minValue = 0;
        int maxValue = 3;

        for (String s : customIndividualGUIScales.keySet()) {
            String val = customIndividualGUIScales.getAsJsonObject(s).get("scale").getAsString();
            String name = customIndividualGUIScales.getAsJsonObject(s).get("name").getAsString();

            Property prop = new Property(name, val, Property.Type.INTEGER, "");
            prop.setDefaultValue(defaultScale);
            prop.setMinValue(minValue);
            prop.setMaxValue(maxValue);
            prop.setComment(s);
            list.add(new ScaleConfigElement(prop).setCustomListEntryClass(SnappingSliderEntry.class));
        }

        return list;
    }
*/

/*
    public List<IConfigElement> getGroupsList()
    {
        List<IConfigElement> list = new ArrayList<>();
        int defaultScale = Minecraft.getMinecraft().gameSettings.guiScale;
        int minValue = 0;
        int maxValue = 3;

        for (String s : customGroupGUIScales.keySet()) {
            String val = customGroupGUIScales.getAsJsonObject(s).get("scale").getAsString();
            String name = customGroupGUIScales.getAsJsonObject(s).get("name").getAsString();

            Property prop = new Property(name, val, Property.Type.INTEGER);
            prop.setDefaultValue(defaultScale);
            prop.setMinValue(minValue);
            prop.setMaxValue(maxValue);
            prop.setComment(s);
            list.add(new ScaleConfigElement(prop).setCustomListEntryClass(SnappingSliderEntry.class));
        }

        return list;
    }
*/

}
