package spazley.scalingguis.gui.guiconfig;

import com.google.gson.JsonObject;
import spazley.scalingguis.ScalingGUIs;
import spazley.scalingguis.config.CustomScales;
import spazley.scalingguis.handlers.ClientEventHandler;
import spazley.scalingguis.handlers.ConfigHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.*;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class GuiConfigSG extends GuiConfig {

    protected static CustomScales customScales = new CustomScales();

    //private GuiScreen SGConfigParent;

    //Category/Screen IDs
    public static final String MAIN_ID = "ScalingGUIsConfigMain";
    protected static final String INDIVIDUAL_ID = "ScalingGUIsConfigIndividual";
    protected static final String GROUP_ID = "ScalingGUIsConfigGroup";
    protected static final String BLACKLIST_ID = "ScalingGUIsConfigBlacklist";
    protected static final String GENERAL_ID = "ScalingGUIsConfigGeneral";

    //Element IDs
    protected static final String NEW_INDIVIDUAL_ID = "AddNewIndividual";
    protected static final String NEW_GROUP_ID = "AddNewGroup";
    protected static final String NEW_BLACKLIST_ID = "AddNewBlacklist";
    protected static final String DELETE_ID = "DeleteEntry";

    //Title Lang
    private static final String MAIN_TITLE = "scalingguis.config.main.title";
    private static final String INDIVIDUAL_TITLE = "scalingguis.config.individual.title";
    private static final String GROUP_TITLE = "scalingguis.config.group.title";
    private static final String BLACKLIST_TITLE = "scalingguis.config.blacklist.title";
    private static final String GENERAL_TITLE = "scalingguis.config.general.title";
    private static final String NEW_INDIVIDUAL_TITLE = "scalingguis.config.individual.add.title";
    private static final String NEW_GROUP_TITLE = "scalingguis.config.group.add.title";
    private static final String NEW_BLACKLIST_TITLE = "scalingguis.config.blacklist.add.title";
    private static final String DELETE_INDIVIDUAL_TITLE = "scalingguis.config.individual.delete.title";
    private static final String DELETE_GROUP_TITLE = "scalingguis.config.group.delete.title";


    public GuiConfigSG(GuiScreen parentScreen, String configID) {
        super(parentScreen, getConfigElements(), ScalingGUIs.MODID, configID, false, false, getTitle(configID));
        //SGConfigParent = parentScreen;
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> mainElements = new ArrayList<>();
        List<IConfigElement> individualElements = new ArrayList<>();
        List<IConfigElement> groupElements = new ArrayList<>();
        List<IConfigElement> blacklistElements = new ArrayList<>();
        List<IConfigElement> generalElements = new ArrayList<>();

        //Main Config Menu Elements
        mainElements.addAll(ConfigHandler.getMainsList());
        //mainElements.addAll((new ConfigElement(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements());

        //Individual Config Menu Elements
        individualElements.addAll(ConfigHandler.getIndividualsList());
        individualElements.add(new DummyConfigElement.DummyCategoryElement(NEW_INDIVIDUAL_ID, NEW_INDIVIDUAL_TITLE, NewIndividualScale.class));
        individualElements.add(new DummyConfigElement.DummyCategoryElement(DELETE_ID, DELETE_INDIVIDUAL_TITLE, DeleteEntry.class));

        //Group Config Menu Elements
        groupElements.addAll(ConfigHandler.getGroupsList());
        groupElements.add(new DummyConfigElement.DummyCategoryElement(NEW_GROUP_ID, NEW_GROUP_TITLE, NewGroupScale.class));
        groupElements.add(new DummyConfigElement.DummyCategoryElement(DELETE_ID, DELETE_GROUP_TITLE, DeleteEntry.class));


        //Blacklist Config Menu Elements
        blacklistElements.addAll(ConfigHandler.getBlacklistElementsList());

        //General Config Menu Elements
        generalElements.addAll((new ConfigElement(ConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements());


        //Main Config Menu Category Elements
        mainElements.add(new DummyConfigElement.DummyCategoryElement(INDIVIDUAL_ID, INDIVIDUAL_TITLE, individualElements));
        mainElements.add(new DummyConfigElement.DummyCategoryElement(GROUP_ID, GROUP_TITLE, groupElements));
        mainElements.add(new DummyConfigElement.DummyCategoryElement(BLACKLIST_ID, BLACKLIST_TITLE, blacklistElements));
        mainElements.add(new DummyConfigElement.DummyCategoryElement(GENERAL_ID, GENERAL_TITLE, generalElements));

        return mainElements;
    }

    private static String getTitle(String configID) {
        switch (configID) {
            case MAIN_ID:
                return I18n.format(MAIN_TITLE);
            case INDIVIDUAL_ID:
                return I18n.format(INDIVIDUAL_TITLE);
            case GROUP_ID:
                return I18n.format(GROUP_TITLE);
        }

        return "";
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        //super.actionPerformed(button);

        if (button.id == 2000) {
            this.entryList.saveConfigElements();
            saveCustomScales();
            this.mc.displayGuiScreen(parentScreen);
        } else if (button.id == 2001) {
            this.entryList.setAllToDefault(this.chkApplyGlobally.isChecked());
        } else if (button.id == 2002) {
            this.entryList.undoAllChanges(this.chkApplyGlobally.isChecked());
        }
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            this.entryList.saveConfigElements();
            saveCustomScales();
            this.mc.displayGuiScreen(parentScreen);
        } else {
            this.entryList.keyTyped(eventChar, eventKey);
        }
    }

    private void saveCustomScales() {
        customScales = ConfigHandler.customScales;
        for (IConfigElement ice : this.configElements) {
            String iceName = ice.getName();
            switch (iceName) {
                case "guiScale":
                    if (customScales.guiScale != Integer.valueOf(ice.get().toString())) {
                        ClientEventHandler.setCancelGuiVideoSettings(false);
                        ScalingGUIs.logger.info("No cancelation");
                    }
                    customScales.guiScale = Integer.valueOf(ice.get().toString());
                    break;
                case "hudScale":
                    customScales.hudScale = Integer.valueOf(ice.get().toString());
                    break;
                case "tooltipScale":
                    customScales.tooltipScale = Integer.valueOf(ice.get().toString());
                    break;
                case INDIVIDUAL_ID:
                    saveIndividuals(ice);
                    break;
                case GROUP_ID:
                    saveGroups(ice);
                    break;
                case BLACKLIST_ID:
                    saveBlacklist(ice);
                    break;
                default:
                    break;
            }
        }

        ConfigHandler.customScales = customScales;
        ConfigHandler.saveConfigs();
        ConfigHandler.initConfigs();
    }

    private void saveIndividuals(IConfigElement iConfigElementIn)
    {
        JsonObject individuals = new JsonObject();

        for (IConfigElement ice : iConfigElementIn.getChildElements()) {
            if (!NEW_INDIVIDUAL_ID.equals(ice.getName()) && !DELETE_ID.equals(ice.getName())) {
                String guiClassName = ice.getComment();
                int guiScale = Integer.valueOf(ice.get().toString());
                String guiDisplayName = ice.getName();

                JsonObject individual = new JsonObject();
                individual.addProperty("scale", guiScale);
                individual.addProperty("name", guiDisplayName);

                individuals.add(guiClassName, individual);
            }
        }

        customScales.customIndividualGuiScales = individuals;
    }

    private void saveGroups(IConfigElement iConfigElementIn)
    {
        JsonObject groups = new JsonObject();

        for (IConfigElement ice : iConfigElementIn.getChildElements()) {
            if (!NEW_GROUP_ID.equals(ice.getName()) && !DELETE_ID.equals(ice.getName())) {
                String guiClassName = ice.getComment();
                int guiScale = Integer.valueOf(ice.get().toString());
                String guiDisplayName = ice.getName();

                JsonObject group = new JsonObject();
                group.addProperty("scale", guiScale);
                group.addProperty("name", guiDisplayName);

                groups.add(guiClassName, group);
            }
        }

        customScales.customGroupGuiScales = groups;
    }

    private void saveBlacklist(IConfigElement iConfigElementIn)
    {
        for (IConfigElement ice : iConfigElementIn.getChildElements()) {
            //((ConfigElement)ice).;
            if ("blacklist".equals(ice.getName())) {
               customScales.blacklistGuiClassNames = new TreeSet<String>(Arrays.asList(((String[])ice.getList())));
            }
        }
    }


    /*
     *  Opens a screen to add a new custom individual GUI entry
     */
    public static class NewIndividualScale extends GuiConfigEntries.CategoryEntry {
        public NewIndividualScale(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            return new GuiNewScale(this.owningScreen, this.owningScreen.modID, NEW_INDIVIDUAL_ID);
        }
    }

    /*
     *  Opens a screen to add a new custom individual GUI entry
     */
    public static class NewGroupScale extends GuiConfigEntries.CategoryEntry {
        public NewGroupScale(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            return new GuiNewScale(this.owningScreen, this.owningScreen.modID, NEW_GROUP_ID);
        }
    }


    public static class GuiNewScale extends GuiConfig {
        //private String entryType;
        private String warningText;
        private boolean invalidClassName = false;

        public GuiNewScale(GuiScreen parentScreen, String modID, String entryType) {
            super(parentScreen, getNewScaleElements(), modID, false, false, getNewScaleTitle(entryType));
            //this.entryType = entryType;
        }

        private static List<IConfigElement> getNewScaleElements() {
            List<IConfigElement> list = new ArrayList<>();
            //int defaultScale = ConfigHandler.customScales.guiScale;
            int defaultScale = ConfigHandler.MAX_SCALE; //Causes the scale to default to the main GUI scale

            list.add(new DummyConfigElement("guiClassNameSelect", "", ConfigGuiType.STRING, "scalingguis.config.add.classselect").setCustomListEntryClass(GuiClassName.class));
            //list.add(new ConfigElement(new Property("guiClassName", "", Property.Type.STRING, "scalingguis.config.add.classname")));
            list.add(new ClassStringElement(new Property("guiClassName", "", Property.Type.STRING, "scalingguis.config.add.classname")).setCustomListEntryClass(GuiClassStringEntry.class));
            list.add(new ConfigElement(new Property("guiDisplayName", "", Property.Type.STRING, "scalingguis.config.add.displayname")));

            Property guiScaleProp = new Property("guiScale", String.valueOf(defaultScale), Property.Type.INTEGER, "scalingguis.config.add.scale");
            guiScaleProp.setDefaultValue(defaultScale);
            guiScaleProp.setMinValue(ConfigHandler.MIN_SCALE);
            guiScaleProp.setMaxValue(ConfigHandler.MAX_SCALE);

            list.add(new ScaleConfigElement(guiScaleProp).setCustomListEntryClass(SnappingSliderEntry.class));


            return list;
        }

        private static String getNewScaleTitle(String entryType) {
            if (entryType.equals(NEW_INDIVIDUAL_ID)) {
                return I18n.format(NEW_INDIVIDUAL_TITLE);
            } else {
                return I18n.format(NEW_GROUP_TITLE);
            }
        }

        private IConfigElement constructNewScaleElement() {
            String guiClassName = "";
            String guiDisplayName = "";
            String guiScale = "";

            for (IConfigElement ice : this.configElements) {
                if ("guiClassName".equals(ice.getName())) {
                    guiClassName = ice.get().toString();
                } else if ("guiDisplayName".equals(ice.getName())) {
                    guiDisplayName = ice.get().toString();
                } else if ("guiScale".equals(ice.getName())) {
                    guiScale = ice.get().toString();
                }
            }
            guiDisplayName = ("".equals(guiDisplayName)) ? guiClassName : guiDisplayName;

            Property guiScaleProp = new Property(guiDisplayName, guiScale, Property.Type.INTEGER);
            guiScaleProp.setComment(guiClassName);
            guiScaleProp.setMinValue(ConfigHandler.MIN_SCALE);
            guiScaleProp.setMaxValue(ConfigHandler.MAX_SCALE);
            guiScaleProp.setDefaultValue(ConfigHandler.customScales.guiScale);

            return new ScaleConfigElement(guiScaleProp).setCustomListEntryClass(SnappingSliderEntry.class);
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (button.id == 2000) {
                this.entryList.saveConfigElements();
                String guiClassName = "";

                for (IConfigElement ice : this.configElements) {
                    if ("guiClassName".equals(ice.getName())) {
                        guiClassName = ice.get().toString();
                        break;
                    }
                }

                if ("".equals(guiClassName)) {
                    this.mc.displayGuiScreen(parentScreen);
                }

                try {
                    Class c = Class.forName(guiClassName); //Check that provided class name is valid
                } catch (Exception e) {

                    invalidClassName = true;
                    warningText = "Unable to determine class for '" + guiClassName + "'.";
                    ScalingGUIs.logger.warn(warningText);

                    return;
                }

                ((GuiConfig) this.parentScreen).configElements.add(((GuiConfig) this.parentScreen).configElements.size() - 2, constructNewScaleElement());
                ((GuiConfig) this.parentScreen).needsRefresh = true;
                this.mc.displayGuiScreen(this.parentScreen);

            } else {
                super.actionPerformed(button);
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);

            if (invalidClassName) {
                drawCenteredString(fontRenderer, warningText, width / 2, (height * 3 / 4), Integer.parseInt("FFAA00", 16));
                //this.drawCenteredString(fontRenderer, warningText, width / 2, height / 4, 0xe0e0e0);
            }

        }

        @Override
        protected void keyTyped(char eventChar, int eventKey)
        {
            if (eventKey == Keyboard.KEY_ESCAPE)
                this.mc.displayGuiScreen(parentScreen);
            else
                this.entryList.keyTyped(eventChar, eventKey);
        }
    }

    public static class GuiClassName extends GuiConfigEntries.SelectValueEntry
    {
        public GuiClassName(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop, getKnownGuiClassNames());
            if (this.selectableValues.size() == 0) {
                this.btnValue.enabled = false;
            }
        }

        private static Map<Object, String> getKnownGuiClassNames() {
            return ConfigHandler.getUnusedLoggedClassNames();
        }

        @Override
        public void setValueFromChildScreen(Object newValue) {
            if (enabled() && currentValue != null ? !currentValue.equals(newValue) : newValue != null) {
                currentValue = newValue;
                updateValueButtonText();

                for (GuiConfigEntries.IConfigEntry ice : this.owningEntryList.listEntries) {
                    if ("guiClassName".equals(ice.getName())) {
                        ((GuiClassStringEntry) ice).setTextField(currentValue.toString());
                    }
                }
            }

        }

        @Override
        public void valueButtonPressed(int slotIndex)
        {
            mc.displayGuiScreen(new GuiSelectStringSG(this.owningScreen, configElement, slotIndex, getKnownGuiClassNames(), currentValue, enabled()));
        }

    }

    public static class DeleteEntry extends GuiConfigEntries.CategoryEntry
    {
        public DeleteEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        @Override
        protected GuiScreen buildChildScreen() {
            ScaleType type = INDIVIDUAL_ID.equals(this.owningScreen.configID) ? ScaleType.INDIVIDUAL : ScaleType.GROUP;
            return new GuiDeleteEntry(this.owningScreen, this.owningScreen.modID, type);
        }

        @Override
        public boolean enabled()
        {
            return (this.owningEntryList.getSize() > 2);
        }
    }

    public static class GuiDeleteEntry extends GuiConfig
    {
        private ScaleType type;
        //private boolean skip = false;

        public GuiDeleteEntry(GuiConfig parentScreen, String modID, ScaleType typeIn)
        {
            super(parentScreen, getDeleteElements(parentScreen), modID, false, false, "titlePlaceholder");
            type = typeIn;
        }

        public static List<IConfigElement> getDeleteElements(GuiConfig parentScreen)
        {
            List<IConfigElement> list = new ArrayList<>();

            list.add(new DummyConfigElement(DELETE_ID, "", ConfigGuiType.STRING, "scalingguis.placeholder").setCustomListEntryClass(SelectDeleteEntry.class));

            return list;
        }

        @Override
        public void initGui()
        {
            super.initGui();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks)
        {
            ListIterator<GuiConfigEntries.IConfigEntry> iter = this.entryList.listEntries.listIterator();

            while (iter.hasNext()) {
                int index = iter.nextIndex();
                GuiConfigEntries.IConfigEntry ice = iter.next();

                ((SelectDeleteEntry)ice).valueButtonPressed(index);

            }
        }

        public void removeEntry(String removeName)
        {
            Iterator<IConfigElement> iter = ((GuiConfig)parentScreen).configElements.iterator();
            while (iter.hasNext()) {
                IConfigElement ice = iter.next();
                if (!NEW_INDIVIDUAL_ID.equals(ice.getName()) && !NEW_BLACKLIST_ID.equals(ice.getName()) && !DELETE_ID.equals(ice.getName())) {
                    if (removeName.equals(ice.getComment())) {
                        //((GuiConfig)parentScreen).configElements.remove(ice);
                        iter.remove();
                        ((GuiConfig)parentScreen).needsRefresh = true;
                        if (type == ScaleType.INDIVIDUAL) {
                            ConfigHandler.removeIndividualClassName(removeName);
                        } else {
                            ConfigHandler.removeGroupClassName(removeName);
                        }
                    }
                }
            }
        }
    }

    public static class SelectDeleteEntry extends GuiConfigEntries.SelectValueEntry
    {
        public SelectDeleteEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
        {
            super(owningScreen, owningEntryList, prop, getDeleteNames(owningScreen));
            if (this.selectableValues.size() == 0) {
                this.btnValue.enabled = false;
            }
        }

        private static Map<Object, String> getDeleteNames(GuiConfig owningScreen)
        {
            Map<Object, String> map = new LinkedHashMap<>();

            for (IConfigElement ice : ((GuiConfig)owningScreen.parentScreen).configElements) {
                if (!NEW_INDIVIDUAL_ID.equals(ice.getName()) && !NEW_GROUP_ID.equals(ice.getName()) && !DELETE_ID.equals(ice.getName())) {
                    String displayName = ice.getName();
                    String className = ice.getComment();
                    map.put(className, displayName + ":  " + className);
                }
            }

            return map;
        }

        @Override
        public void valueButtonPressed(int slotIndex)
        {
            mc.displayGuiScreen(new GuiSelectDeleteSG(this.owningScreen, configElement, slotIndex, selectableValues, currentValue, enabled()));
        }

        @Override
        public void setValueFromChildScreen(Object newValue)
        {
            if (enabled() && currentValue != null ? !currentValue.equals(newValue) : newValue != null)
            {
                //currentValue = newValue;
                //updateValueButtonText();
                ((GuiDeleteEntry)owningScreen).removeEntry((String)newValue);
            }
        }
    }
}