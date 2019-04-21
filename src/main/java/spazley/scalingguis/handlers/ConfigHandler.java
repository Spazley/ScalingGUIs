package spazley.scalingguis.handlers;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.common.Loader;
import spazley.scalingguis.ScalingGUIs;
import spazley.scalingguis.config.CustomScales;
import spazley.scalingguis.config.JsonHelper;
import spazley.scalingguis.gui.guiconfig.ScaleConfigElement;
import spazley.scalingguis.gui.guiconfig.SnappingSliderEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.io.*;
import java.util.*;

public class ConfigHandler
{
    public static Configuration config;
    public static CustomScales customScales;

    private static List<String> individualGuiClassNames;
    private static List<String> groupGuiClassNames;
    private static List<String> blacklistGuiClassNames;

    public static boolean logGuiClassNames = false;
    public static boolean persistentLog = true;
    public static boolean addDefaultBlacklist = true;
    public static boolean logGuiClassNamesChat = false;
    private static boolean sortLoggedAlphabetically = false;

    //public static String configPath = "config/ScalingGUIs/ScalingGUIs.cfg";
    public static String scaleOverridesPath = "config/ScalingGUIs/ScalingGUIsCustomScales.json";

    public static final int MIN_SCALE = 0;
    public static final int MAX_SCALE = 9; //max actual scale of 8x. +1 to allow sliders to have default scale option


    public static void initConfigs()
    {
        if(config == null) {
            ScalingGUIs.logger.error("Attempted to load config before initialized.");
            return;
        }

        config.load();

        logGuiClassNames = config.getBoolean("logNames", Configuration.CATEGORY_GENERAL, false, "Enable logging of GUI class names in the Minecraft log.");
        persistentLog = config.getBoolean("persistentLog", Configuration.CATEGORY_GENERAL, true, "Maintain persistent log of GUI class names.");
        addDefaultBlacklist = config.getBoolean("updateBlacklist", Configuration.CATEGORY_GENERAL, true, "Update the blacklist with the current default blacklist. Will not remove custom blacklist entries.");
        logGuiClassNamesChat = config.getBoolean("logToChat", Configuration.CATEGORY_GENERAL, false, "Display opened GUI class names in the ingame chat.");
        sortLoggedAlphabetically = config.getBoolean("sortNames", Configuration.CATEGORY_GENERAL, false, "Diplay logged GUI class names alphabetically in select-value screens.");
        //Collections.addAll(loggedGuiClassNames, config.getStringList("Logged GUI Class Names", "log", new String[0], "Persistent log of GUI class names. Updated on config save."));

        config.save();


        File fileScaleOverrides = new File(scaleOverridesPath);
        customScales = JsonHelper.scalesFromJsonFile(fileScaleOverrides);
        customScales.checkCustomEntries();
        if (addDefaultBlacklist)
        {
            Map<String, List<String>> map = JsonHelper.defaultBlacklistsFromJsonFile();

            for (String key : map.keySet()) {
                if (Loader.isModLoaded(key)) {
                    customScales.blacklistGuiClassNames.addAll(map.get(key));
                }
            }
        }
        JsonHelper.scalesToJsonFile(fileScaleOverrides, customScales);

        individualGuiClassNames = JsonHelper.getKeyList(customScales.customIndividualGuiScales);
        groupGuiClassNames = JsonHelper.getKeyList(customScales.customGroupGuiScales);
        blacklistGuiClassNames = new ArrayList<>(customScales.blacklistGuiClassNames);
    }

    public static void saveConfigs()
    {
        ScalingGUIs.logger.info("Saving configs");
        File fileScaleOverrides = new File(scaleOverridesPath);

        Minecraft.getMinecraft().gameSettings.guiScale = customScales.guiScale;
        Minecraft.getMinecraft().gameSettings.saveOptions();
        config.save();
        JsonHelper.scalesToJsonFile(fileScaleOverrides, customScales);
        initConfigs();
    }

    public static boolean inIndividuals(String className)
    {
        return individualGuiClassNames.contains(className);
    }

    public static String inGroups(GuiScreen guiScreen)
    {
        for (String s : groupGuiClassNames) {
            try {
                Class<?> c = Class.forName(s);
                if (c.isInstance(guiScreen)) {
                    //ScalingGUIs.logger.info("Groups contains '" + guiScreen.getClass().getName() + "'.");
                    return s;
                }
            } catch(Exception e) {
                ScalingGUIs.logger.error("Unable to determine class for '" + s + "': ", e);
            }

        }

        return "NONE";
    }

    public static boolean inBlacklist(String className)
    {
        return blacklistGuiClassNames.contains(className);
    }

    public static int getIndividualScale(String className)
    {
        int scale = customScales.customIndividualGuiScales.getAsJsonObject(className).getAsJsonPrimitive("scale").getAsInt();
        return scale == MAX_SCALE ? customScales.guiScale : scale;
    }

    public static int getGroupScale(String className)
    {
        int scale = customScales.customGroupGuiScales.getAsJsonObject(className).getAsJsonPrimitive("scale").getAsInt();
        return scale == MAX_SCALE ? customScales.guiScale : scale;
    }

    public static int getGuiScale(GuiScreen guiScreen)
    {
        if (inIndividuals(guiScreen.getClass().getName())) {
            return getIndividualScale(guiScreen.getClass().getName());
        } else if (!"NONE".equals(inGroups(guiScreen))) {
            return getGroupScale(inGroups(guiScreen));
        } else if (guiScreen instanceof GuiChat) {
            return customScales.hudScale;
        } else {
            return customScales.guiScale;
        }
    }

    public static int getTooltipScale()
    {
        return customScales.tooltipScale == MAX_SCALE ? customScales.guiScale : customScales.tooltipScale;
    }

    public static int getHudScale()
    {
        return customScales.hudScale == MAX_SCALE ? customScales.guiScale : customScales.hudScale;
    }


    public static List<IConfigElement> getMainsList()
    {
        List<IConfigElement> list = new ArrayList<>();
        int defaultScale = MAX_SCALE; //Causes the scale to default to the main GUI scale

        Property guiScaleProp = new Property("guiScale", String.valueOf(customScales.guiScale), Property.Type.INTEGER, "scalingguis.config.main.guiscale");
        guiScaleProp.setDefaultValue(defaultScale);
        guiScaleProp.setMinValue(MIN_SCALE);
        guiScaleProp.setMaxValue(MAX_SCALE);
        list.add(new ScaleConfigElement(guiScaleProp).setCustomListEntryClass(SnappingSliderEntry.class));

        Property hudScaleProp = new Property("hudScale", String.valueOf(customScales.hudScale), Property.Type.INTEGER, "scalingguis.config.main.hudscale");
        hudScaleProp.setDefaultValue(defaultScale);
        hudScaleProp.setMinValue(MIN_SCALE);
        hudScaleProp.setMaxValue(MAX_SCALE);
        list.add(new ScaleConfigElement(hudScaleProp).setCustomListEntryClass(SnappingSliderEntry.class));

        Property tooltipScaleProp = new Property("tooltipScale", String.valueOf(customScales.tooltipScale), Property.Type.INTEGER, "scalingguis.config.main.tooltipscale");
        tooltipScaleProp.setDefaultValue(defaultScale);
        tooltipScaleProp.setMinValue(MIN_SCALE);
        tooltipScaleProp.setMaxValue(MAX_SCALE);
        list.add(new ScaleConfigElement(tooltipScaleProp).setCustomListEntryClass(SnappingSliderEntry.class));

        return list;
    }

    public static List<IConfigElement> getIndividualsList()
    {
        List<IConfigElement> list = new ArrayList<>();
        int defaultScale = customScales.guiScale;

        for (String s : JsonHelper.getKeyList(customScales.customIndividualGuiScales)) {
            String val = customScales.customIndividualGuiScales.getAsJsonObject(s).get("scale").getAsString();
            String name = customScales.customIndividualGuiScales.getAsJsonObject(s).get("name").getAsString();

            Property prop = new Property(name, val, Property.Type.INTEGER, "");
            prop.setDefaultValue(defaultScale);
            prop.setMinValue(MIN_SCALE);
            prop.setMaxValue(MAX_SCALE);
            prop.setComment(s);
            list.add(new ScaleConfigElement(prop).setCustomListEntryClass(SnappingSliderEntry.class));
        }

        return list;
    }

    public static List<IConfigElement> getGroupsList()
    {
        List<IConfigElement> list = new ArrayList<>();
        int defaultScale = customScales.guiScale;

        for (String s : JsonHelper.getKeyList(customScales.customGroupGuiScales)) {
            String val = customScales.customGroupGuiScales.getAsJsonObject(s).get("scale").getAsString();
            String name = customScales.customGroupGuiScales.getAsJsonObject(s).get("name").getAsString();

            Property prop = new Property(name, val, Property.Type.INTEGER);
            prop.setDefaultValue(defaultScale);
            prop.setMinValue(MIN_SCALE);
            prop.setMaxValue(MAX_SCALE);
            prop.setComment(s);
            list.add(new ScaleConfigElement(prop).setCustomListEntryClass(SnappingSliderEntry.class));
        }

        return list;
    }

    public static List<IConfigElement> getBlacklistElementsList()
    {
        List<IConfigElement> list = new ArrayList<>();

        Property prop = new Property("blacklist", blacklistGuiClassNames.toArray(new String[0]), Property.Type.STRING, "scalingguis.config.blacklist.title");

        list.add(new ConfigElement(prop));

        return list;
    }

    public static Map<Object, String> getUnusedLoggedClassNames()
    {
        List<String> list = new ArrayList<>(customScales.loggedGuiClassNames);

        list.removeAll(individualGuiClassNames);
        list.removeAll(groupGuiClassNames);
        list.removeAll(blacklistGuiClassNames);

        if (sortLoggedAlphabetically) {
            Collections.sort(list);
        } else {
            Collections.reverse(list);
        }

        Map<Object, String> map = new LinkedHashMap<>();

        for (String s : list) {
            map.put((Object)s,s);
        }

        return map;
    }

    public static void logClassName(String className)
    {
        customScales.loggedGuiClassNames.remove(className);
        customScales.loggedGuiClassNames.add(className);
    }

    public static void removeIndividualClassName(String className)
    {
        individualGuiClassNames.remove(className);
    }

    public static void removeGroupClassName(String className)
    {
        groupGuiClassNames.remove(className);
    }

}
