package spazley.scalingguis.gui.guiconfig;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiSelectStringEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GuiSelectStringEntriesSG extends GuiSelectStringEntries {

    public GuiSelectStringEntriesSG(GuiSelectStringSG owningScreen, Minecraft mc, IConfigElement configElement, Map<Object, String> selectableValues)
    {
        super(owningScreen, mc, configElement, selectableValues);


        //listEntries = new ArrayList<IGuiSelectStringListEntry>();
        listEntries.clear();

        int index = 0;
        //List<Entry<Object, String>> sortedList = new ArrayList<Entry<Object, String>>(selectableValue);
        List<Entry<Object, String>> sortedList = new ArrayList<Entry<Object, String>>(selectableValues.entrySet());
        //Collections.sort(sortedList, new EntryComparator()); Do not sort the values

        for (Entry<Object, String> entry : sortedList)
        {
            listEntries.add(new ListEntry(this, entry));
            if (mc.fontRenderer.getStringWidth(entry.getValue()) > maxEntryWidth)
                maxEntryWidth = mc.fontRenderer.getStringWidth(entry.getValue());

            if (owningScreen.currentValue.equals(entry.getKey()))
            {
                this.selectedIndex = index;
            }

            index++;
        }
    }


}
