package spazley.scalingguis.config;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;

import java.util.*;

public class CustomScales {
    public int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
    public int hudScale = Minecraft.getMinecraft().gameSettings.guiScale;
    public int tooltipScale = Minecraft.getMinecraft().gameSettings.guiScale;
    public JsonObject customIndividualGuiScales = new JsonObject();
    public JsonObject customGroupGuiScales = new JsonObject();
    //public SortedSet<String> loggedGuiClassNames = new TreeSet<>();
    public Set<String> loggedGuiClassNames = new LinkedHashSet<>();
    //public List<String> loggedGuiClassNames = new ArrayList<>();
    public SortedSet<String> blacklistGuiClassNames = new TreeSet<>();// blacklistDefault();


    public void checkCustomEntries()
    {
        for (String s : JsonHelper.getKeyList(customIndividualGuiScales)) {
            if (!customIndividualGuiScales.getAsJsonObject(s).has("scale")) {
                customIndividualGuiScales.getAsJsonObject(s).addProperty("scale", Minecraft.getMinecraft().gameSettings.guiScale);
            }
            if (!customIndividualGuiScales.getAsJsonObject(s).has("name")) {
                customIndividualGuiScales.getAsJsonObject(s).addProperty("name", s);
            }
        }
        for (String s : JsonHelper.getKeyList(customGroupGuiScales)) {
            if (!customGroupGuiScales.getAsJsonObject(s).has("scale")) {
                customGroupGuiScales.getAsJsonObject(s).addProperty("scale", Minecraft.getMinecraft().gameSettings.guiScale);
            }
            if (!customGroupGuiScales.getAsJsonObject(s).has("name")) {
                customGroupGuiScales.getAsJsonObject(s).addProperty("name", s);
            }
        }
    }

/*    public SortedSet<String> blacklistDefault()
    {
        SortedSet<String> blacklist = new TreeSet<>();

        Map<String, String[]> map = DefaultBlacklist.getDefaultBlacklist();

        for (String key : map.keySet()) {
            if (Loader.isModLoaded(key)) {
                blacklist.addAll(Arrays.asList(map.get(key)));
            }
        }

        return blacklist;
    }*/

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

        for (String s : customIndividualGuiScales.keySet()) {
            String val = customIndividualGuiScales.getAsJsonObject(s).get("scale").getAsString();
            String name = customIndividualGuiScales.getAsJsonObject(s).get("name").getAsString();

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

        for (String s : customGroupGuiScales.keySet()) {
            String val = customGroupGuiScales.getAsJsonObject(s).get("scale").getAsString();
            String name = customGroupGuiScales.getAsJsonObject(s).get("name").getAsString();

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
