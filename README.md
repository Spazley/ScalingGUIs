# ScalingGUIs
Decouples GUI scales, HUD scale, and Tooltip scale such that any GUI can be scaled to the player's liking.

[Downloads](https://github.com/Spazley/ScalingGUIs/releases)

## Default Scales
ScalingGUIs includes settings for GUI, HUD, and Tooltip scales. 
* GUI scale the default scale for all GUIs
* HUD scale for rendering all HUD elements
* Tooltip scale for rendering tooltips

## Custom Individual and Group Scales
ScalingGUIs allows for custom scales for GUIs based on user-specified GUI class names.
* `net.minecraft.client.gui.GuiMainMenu` allows the user to specify a scale for the Main Menu
* `betterquesting.api2.client.gui.GuiScreenCanvas` allows the user to specify a scale for Better Questing GUIs

The Individual and Group sets of GUIs are functionally identical. ScalingGUIs first checks whether a new GUI is an instance of a class in the Individual set then the Group set. 
* An Individual entry for `spazley.scalingguis.gui.guiconfig.GuiConfigSG` would allow the ScalingGUIs config GUI to scale differently than any config GUI based on a Group entry for `net.minecraftforge.fml.client.config.GuiConfig`. 

## Scale Slider
ScalingGUIs presents the scale settings as sliders. The scales range from 1x (Small) to 8x*. The left-most slider position is the Auto (Max) scale. The right-most slider position sets the scale to the default/main GUI scale.

## Class Name Logging
ScalingGUIs allows logging of GUI class names in the Minecraft log and persistent logging in the `ScalingGUIsCustomScales.json` config file.

## TODO
* Set up the wiki to better describe operation and functionality
* Add SelectValue entry to blacklist config category as in the Individual and Group categories
* Add system to recouple GUI scales.  Will allow for a single slider to scale multiple differently-named GUIs
