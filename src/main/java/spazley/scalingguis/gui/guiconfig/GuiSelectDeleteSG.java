package spazley.scalingguis.gui.guiconfig;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiSelectStringEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.FMLLog;
import org.lwjgl.input.Keyboard;

import java.util.Map;

public class GuiSelectDeleteSG extends GuiSelectStringSG
{
    public GuiSelectDeleteSG(GuiScreen parentScreen, IConfigElement configElement, int slotIndex, Map<Object, String> selectableValues, Object currentValue, boolean enabled)
    {
        super(parentScreen, configElement, slotIndex, selectableValues, currentValue, enabled);
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey)
    {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(((GuiConfig)parentScreen).parentScreen);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            try
            {
                this.entryList.saveChanges();
            }
            catch (Throwable e)
            {
                FMLLog.log.error("Error performing GuiSelectString action:", e);
            }
            this.mc.displayGuiScreen(((GuiConfig)parentScreen).parentScreen);
        }
        else if (button.id == 2001)
        {
            this.currentValue = configElement.getDefault();
            this.entryList = new GuiSelectStringEntries(this, this.mc, this.configElement, this.selectableValues);
        }
        else if (button.id == 2002)
        {
            this.currentValue = beforeValue;
            this.entryList = new GuiSelectStringEntries(this, this.mc, this.configElement, this.selectableValues);
        }
    }


}
