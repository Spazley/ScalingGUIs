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

}
