package spazley.scalingguis.gui.guiconfig;

import spazley.scalingguis.handlers.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;

public class GuiSnappingSlider extends net.minecraftforge.fml.client.config.GuiSlider {

    private boolean snap = true;
    private int intVal;

    public GuiSnappingSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr)
    {
        this(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, null);
    }

    public GuiSnappingSlider(int id, int xPos, int yPos, int width, int height, String prefix, String suf, double minVal, double maxVal, double currentVal, boolean showDec, boolean drawStr, @Nullable ISlider par)
    {
        super(id, xPos, yPos, width, height, prefix, suf, minVal, maxVal, currentVal, showDec, drawStr, par);
        updateSlider();
    }

    @Override
    public int getHoverState(boolean par1)
    {
        return super.getHoverState(par1);
    }

    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        super.mouseDragged(par1Minecraft, par2, par3);
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return super.mousePressed(par1Minecraft, par2, par3);
    }

    //Modified to snap slider button to center of integer's region (or edges for end values)
    @Override
    public void updateSlider()
    {
        if (this.sliderValue < 0.0F)
        {
            this.sliderValue = 0.0F;
        }

        if (this.sliderValue > 1.0F)
        {
            this.sliderValue = 1.0F;
        }

        String val;

        if (showDecimal)
        {
            val = Double.toString(sliderValue * (maxValue - minValue) + minValue);

            if (val.substring(val.indexOf(".") + 1).length() > precision)
            {
                val = val.substring(0, val.indexOf(".") + precision + 1);

                if (val.endsWith("."))
                {
                    val = val.substring(0, val.indexOf(".") + precision);
                }
            }
            else
            {
                while (val.substring(val.indexOf(".") + 1).length() < precision)
                {
                    val = val + "0";
                }
            }
        }
        else
        {
            intVal = (int)Math.round(sliderValue * (maxValue - minValue) + minValue);
            val = Integer.toString(intVal);
        }

        if(snap)
        {
            sliderValue = (double)intVal / (maxValue - minValue);
        }

        final int AUTO = 0; //Far left of slider
        final int SMALL = 1;
        final int NORMAL = 2;
        final int LARGE = 3;
        final int DEFAULT = ConfigHandler.MAX_SCALE; //Far right of slider

        if(drawString) {
            switch (intVal) {
                case AUTO:      displayString = I18n.format("scalingguis.slidertext.auto");
                                break;
                case SMALL:     displayString = I18n.format("scalingguis.slidertext.small");
                                break;
                case NORMAL:    displayString = I18n.format("scalingguis.slidertext.normal");
                                break;
                case LARGE:     displayString = I18n.format("scalingguis.slidertext.large");
                                break;
                case DEFAULT:   displayString = I18n.format("scalingguis.slidertext.default");
                                break;
                default:        displayString = dispString + val + suffix;
                                break;
            }
        }

        if (parent != null)
        {
            parent.onChangeSliderValue(this);
        }

    }

    @Override
    public void mouseReleased(int par1, int par2)
    {
        super.mouseReleased(par1, par2);
    }

    public int getValueInt()
    {
        return super.getValueInt();
    }

    public double getValue()
    {
        return super.getValue();
    }

    public void setValue(double d)
    {
        super.setValue(d);
    }

}
