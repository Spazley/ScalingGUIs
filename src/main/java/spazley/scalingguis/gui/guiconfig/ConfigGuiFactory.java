package spazley.scalingguis.gui.guiconfig;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class ConfigGuiFactory implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraftInstance) {}

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
        return new GuiConfigSG(parentScreen, GuiConfigSG.MAIN_ID);
    }

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

/*
    public static class SGConfigGui extends GuiConfig
    {
        public SGConfigGui(GuiScreen parentScreen)
        {
            super(parentScreen, getConfigElements(), ScalingGUIs.MODID, false, false, "scalingguis.config.title");
        }

        private static List<IConfigElement> getConfigElements()
        {

        }
    }
*/
}