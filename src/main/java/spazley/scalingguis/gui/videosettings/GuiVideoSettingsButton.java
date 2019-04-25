package spazley.scalingguis.gui.videosettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.client.FMLClientHandler;
import spazley.scalingguis.gui.guiconfig.GuiConfigSG;
import spazley.scalingguis.handlers.ClientEventHandler;

public class GuiVideoSettingsButton extends GuiButton
{
    public GuiVideoSettingsButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean superPressed = super.mousePressed(mc, mouseX, mouseY);

        if (superPressed) {
            if (!FMLClientHandler.instance().hasOptifine()) this.playPressSound(mc.getSoundHandler());
            mc.displayGuiScreen(new GuiConfigSG(mc.currentScreen, GuiConfigSG.MAIN_ID));
            ClientEventHandler.setCancelGuiVideoSettings(true); //Prevent GuiVideoSettings from changing scale after opening GuiConfigSG
        }

        return superPressed;
    }
}
