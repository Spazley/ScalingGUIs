package spazley.scalingguis.gui.guiconfig;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries;

public class ClassStringElement extends ConfigElement {

    private Class<? extends GuiConfigEntries.IConfigEntry> configEntryClass;


    public ClassStringElement(Property prop)
    {
        super(prop);
    }

    public ClassStringElement setCustomListEntryClass(Class<? extends GuiConfigEntries.IConfigEntry> clazz)
    {
        this.configEntryClass = clazz;
        return this;
    }

    @Override
    public Class<? extends GuiConfigEntries.IConfigEntry> getConfigEntryClass()
    {
        return configEntryClass;
    }

}
