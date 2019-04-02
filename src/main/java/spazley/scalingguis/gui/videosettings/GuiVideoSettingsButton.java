package spazley.scalingguis.gui.videosettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import spazley.scalingguis.gui.guiconfig.GuiConfigSG;

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
            this.playPressSound(mc.getSoundHandler());
            mc.displayGuiScreen(new GuiConfigSG(mc.currentScreen, GuiConfigSG.MAIN_ID));
        }

        return superPressed;
    }
}
