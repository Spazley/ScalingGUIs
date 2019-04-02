package spazley.scalingguis.gui.guiconfig;


import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

public class SnappingSliderEntry extends GuiConfigEntries.ButtonEntry {

    public final double beforeValue;

    public SnappingSliderEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement,
                new GuiSnappingSlider(0,owningEntryList.controlX,0,owningEntryList.controlWidth,18,
                        "","", Double.valueOf(configElement.getMinValue().toString()),
                        Double.valueOf(configElement.getMaxValue().toString()), Double.valueOf(configElement.get().toString()),
                        configElement.getType() == ConfigGuiType.DOUBLE, true));

        if (configElement.getType() == ConfigGuiType.INTEGER)
            this.beforeValue = Integer.valueOf(configElement.get().toString());
        else
            this.beforeValue = Double.valueOf(configElement.get().toString());
    }

    @Override
    public void updateValueButtonText()
    {
        ((GuiSnappingSlider) this.btnValue).updateSlider();
    }

    @Override
    public void valueButtonPressed(int slotIndex) {}

    @Override
    public boolean isDefault()
    {
        if (configElement.getType() == ConfigGuiType.INTEGER)
            return ((GuiSnappingSlider) this.btnValue).getValueInt() == Integer.valueOf(configElement.getDefault().toString());
        else
            return ((GuiSnappingSlider) this.btnValue).getValue() == Double.valueOf(configElement.getDefault().toString());
    }

    @Override
    public void setToDefault()
    {
        if (this.enabled())
        {
            ((GuiSnappingSlider) this.btnValue).setValue(Double.valueOf(configElement.getDefault().toString()));
            ((GuiSnappingSlider) this.btnValue).updateSlider();
        }
    }

    @Override
    public boolean isChanged()
    {
        if (configElement.getType() == ConfigGuiType.INTEGER)
            return ((GuiSnappingSlider) this.btnValue).getValueInt() != (int) Math.round(beforeValue);
        else
            return ((GuiSnappingSlider) this.btnValue).getValue() != beforeValue;
    }

    @Override
    public void undoChanges()
    {
        if (this.enabled())
        {
            ((GuiSnappingSlider) this.btnValue).setValue(beforeValue);
            ((GuiSnappingSlider) this.btnValue).updateSlider();
        }
    }

    @Override
    public boolean saveConfigElement()
    {
        if (this.enabled() && this.isChanged())
        {
            if (configElement.getType() == ConfigGuiType.INTEGER)
                configElement.set(((GuiSnappingSlider) this.btnValue).getValueInt());
            else
                configElement.set(((GuiSnappingSlider) this.btnValue).getValue());
            return configElement.requiresMcRestart();
        }
        return false;
    }

    @Override
    public Object getCurrentValue()
    {
        if (configElement.getType() == ConfigGuiType.INTEGER)
            return ((GuiSnappingSlider) this.btnValue).getValueInt();
        else
            return ((GuiSnappingSlider) this.btnValue).getValue();
    }

    @Override
    public Object[] getCurrentValues()
    {
        return new Object[] { getCurrentValue() };
    }

    @Override
    public void onGuiClosed()
    {
        this.saveConfigElement();
    }

}
