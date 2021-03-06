package spazley.scalingguis.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import spazley.scalingguis.ScalingGUIs;
import spazley.scalingguis.gui.videosettings.GuiVideoSettingsButton;

import java.util.List;
import java.util.ListIterator;

public class ClientEventHandler {
    public ClientEventHandler()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }


    private String lastGui = null;
    private int lastScale = -1;

    private boolean renderGuiTakenOver = false;
    private boolean ingameRenderTakenOver = false;

    private static boolean cancelGuiVideoSettings = false; //Prevent GuiVideoSettings from changing scale after opening GuiConfigSG


    //Update game scale to reflect setting for opened GUI.
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void guiPreInit(GuiScreenEvent.InitGuiEvent.Pre e)
    {
        if (e.getGui() != null) {
            try{
                String name = e.getGui().getClass().getName();
                /*if ((name.equals(lastGui) && Minecraft.getMinecraft().gameSettings.guiScale == lastScale)) {
                    return;
                }*/
                if (renderGuiTakenOver) {
                    renderGuiTakenOver = false;
                    return;
                }
                if (ConfigHandler.inBlacklist(name)) {
                    return;
                }
                //Prevent GuiVideoSettings from changing scale after opening GuiConfigSG
                if ((e.getGui() instanceof GuiVideoSettings) && cancelGuiVideoSettings) {
                    e.setCanceled(true);
                    setCancelGuiVideoSettings(false);
                    return;
                }

                if (ConfigHandler.logGuiClassNames && !name.equals(lastGui)) {
                    ScalingGUIs.logger.info("Opened GUI: " + name);
                }
                if (ConfigHandler.logGuiClassNamesChat && !name.equals(lastGui) && Minecraft.getMinecraft().world != null)
                {
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Opened GUI: " + name));
                }
                if (ConfigHandler.persistentLog && !name.equals(lastGui)) {
                    ConfigHandler.logClassName(name);
                }

                lastGui = name;

                if (ConfigHandler.inDynamics(name) && e.getGui() instanceof GuiContainer) {
                    renderGuiTakenOver = true;
                    int xSize = ((GuiContainer)e.getGui()).getXSize();
                    int ySize = ((GuiContainer)e.getGui()).getYSize();
                    Minecraft.getMinecraft().gameSettings.guiScale = ConfigHandler.customScales.guiScale;
                    int i;
                    int w = 0;
                    int h = 0;
                    for (i = (new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor()); i >= 0; i--) {
                        Minecraft.getMinecraft().gameSettings.guiScale = i;
                        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                        w = scaledResolution.getScaledWidth();
                        h = scaledResolution.getScaledHeight();
                        if (w > xSize && h > ySize) {
                            break;
                        }
                    }
                    lastScale = i;

                    e.setCanceled(true);
                    e.getGui().setWorldAndResolution(Minecraft.getMinecraft(), w, h);
                    //renderGuiTakenOver = false;
                    return;
                }

                int newScale = ConfigHandler.getGuiScale(e.getGui());
                if (Minecraft.getMinecraft().gameSettings.guiScale == newScale) {
                    return;
                }
                lastScale = newScale;
                //ScalingGUIs.logger.info("GUI is NOT null. Setting scale to " + newScale + ".");

                renderGuiTakenOver = true;
                Minecraft.getMinecraft().gameSettings.guiScale = newScale;
                ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                int w = scaledResolution.getScaledWidth();
                int h = scaledResolution.getScaledHeight();
                e.setCanceled(true);
                e.getGui().setWorldAndResolution(Minecraft.getMinecraft(), w, h);
                //renderGuiTakenOver = false;

            } catch(Exception ex) {
                //ScalingGUIs.logger.warn("Error in onGuiOpen (if): ", ex);
                renderGuiTakenOver = false;
            }
        } else {
            try{
                lastGui = null;
                lastScale = -1;
                //ScalingGUIs.logger.info("GUI IS NULL.");

            } catch(Exception ex) {
                //ScalingGUIs.logger.warn("Error in onGuiOpen (else): ", ex);
            }
        }
    }

    //Copied from Vise and modified to fit the context of ScalingGUIs. See license.
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onPreRenderGameOverlay(RenderGameOverlayEvent.Pre e)
    {
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (ingameRenderTakenOver) {
                return;
            }
            int newScale = ConfigHandler.getHudScale();
            int oldScale = Minecraft.getMinecraft().gameSettings.guiScale;
            try {
                Minecraft.getMinecraft().gameSettings.guiScale = newScale;
                ingameRenderTakenOver = true;
                e.setCanceled(true);
                Minecraft.getMinecraft().ingameGUI.renderGameOverlay(e.getPartialTicks());
                ingameRenderTakenOver = false;
            } finally {
                Minecraft.getMinecraft().gameSettings.guiScale = oldScale;
                Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
            }
        }
    }

    //Causes toasts to scale with the HUD while no GUI is open
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    //public void onRenderTick(TickEvent.RenderTickEvent e) // Also works
    public void onClientTick(TickEvent.ClientTickEvent e)
    {

        if (Minecraft.getMinecraft().currentScreen == null)
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.gameSettings.guiScale = ConfigHandler.getHudScale();
        }
    }


    //Copied from Vise and modified to fit the context of ScalingGUIs. See license.
    //Set to LOWEST to ensure the scale is set after More Overlays renders its itemsearch overlay.
    //May need to play around with priority if other mods render things with the RenderTooltipEvent.Pre event
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onPreRenderTooltip(RenderTooltipEvent.Pre e) {
        int newScale = clampScale(ConfigHandler.getTooltipScale());
        //ScalingGUIs.logger.info("Setting TOOLTIP scale to " + newScale + ".");
        GlStateManager.pushMatrix();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        float sF = (float)res.getScaleFactor();
        float f = newScale/(float)res.getScaleFactor();
        GlStateManager.scale(f, f, f);
        e.setX((int)(e.getX()/f));
        e.setY((int)(e.getY()/f));
        e.setScreenWidth((int)(e.getScreenWidth()/f));
        e.setScreenHeight((int)(e.getScreenHeight()/f));
        e.setMaxWidth((int)(e.getMaxWidth()/f));
    }

    //Copied from Vise. See license.
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onPostRenderTooltip(RenderTooltipEvent.PostText e) {
        GlStateManager.popMatrix();
        if (Minecraft.getMinecraft().currentScreen != null) {
            GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
            //ScalingGUIs.logger.info(currentScreen.getClass().getName());
        }
    }

    //Copied from Vise. See license.
    private static int getMaxScale() {
        Minecraft mc = Minecraft.getMinecraft();
        int maxScaleW = (mc.displayWidth/320);
        int maxScaleH = (mc.displayHeight/240);
        int maxScale = Math.min(maxScaleW, maxScaleH);
        return maxScale;
    }

    //Copied from Vise. See license.
    private int clampScale(int scale) {
        int max = getMaxScale();
        if (scale == 0 || scale > max) {
            return max;
        }
        return scale;
    }


    // Alter the GuiVideoSettings to set the scale button to open a custom SG scales GUI
    @SubscribeEvent
    public void onPostInit(GuiScreenEvent.InitGuiEvent.Post e)
    {
        if (e.getGui() instanceof GuiVideoSettings) {
            GuiVideoSettings gvs = (GuiVideoSettings) e.getGui();
            if (FMLClientHandler.instance().hasOptifine()) {
                //replaceGuiScaleOptionOptifine(gvs);
                List<GuiButton> buttonList = e.getButtonList();
                e.setButtonList(replaceGuiScaleOptionOptifine(buttonList));
            } else {
                replaceGuiScaleOption(gvs);
            }
        }
    }

    public void replaceGuiScaleOption(GuiVideoSettings gvs)
    {
        List<GuiOptionsRowList.Row> options = ((GuiOptionsRowList)gvs.optionsRowList).options;

        int i = 0;
        for (GuiOptionsRowList.Row row : options) {
            GuiButton buttonA = row.buttonA;
            GuiButton buttonB = row.buttonB;
            boolean found = false;
            if (buttonA instanceof GuiOptionButton && ((GuiOptionButton)buttonA).getOption() == GameSettings.Options.GUI_SCALE) {
                buttonA = new GuiVideoSettingsButton(row.buttonA.id, buttonA.x, buttonA.y, buttonA.width, buttonA.height, I18n.format("scalingguis.videosettings.button"));
                found = true;
                //ScalingGUIs.logger.info("Found scale button in buttonA.");
            }
            if (buttonB instanceof GuiOptionButton && ((GuiOptionButton)buttonB).getOption() == GameSettings.Options.GUI_SCALE) {
                buttonB = new GuiVideoSettingsButton(row.buttonB.id, buttonB.x, buttonB.y, buttonB.width, buttonB.height, I18n.format("scalingguis.videosettings.button"));
                found = true;
                //ScalingGUIs.logger.info("Found scale button in buttonB.");
            }
            if (found) {
                options.set(i, new GuiOptionsRowList.Row(buttonA, buttonB));
                break;
            }
            i++;
        }
    }

    //public void replaceGuiScaleOptionOptifine(GuiVideoSettings gvs)
    public List<GuiButton> replaceGuiScaleOptionOptifine(List<GuiButton> buttonList)
    {
       //ListIterator<GuiButton> iter = gvs.buttonList.listIterator();

       ListIterator<GuiButton> iter = buttonList.listIterator();

       while (iter.hasNext()) {
           int index = iter.nextIndex();
           GuiButton guiButton = iter.next();

           if (guiButton instanceof GuiOptionButton && ((GuiOptionButton)guiButton).getOption() == GameSettings.Options.GUI_SCALE) {
               GuiButton newGuiButton = new GuiVideoSettingsButton(guiButton.id, guiButton.x, guiButton.y, guiButton.width, guiButton.height, I18n.format("scalingguis.videosettings.button"));
               //gvs.buttonList.set(index, newGuiButton);
               buttonList.set(index, newGuiButton);
               break;
           }
       }

       return buttonList;
    }

    public static void setCancelGuiVideoSettings(boolean bool)
    {
        cancelGuiVideoSettings = bool;
    }
}
