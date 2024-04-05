package com.nali.chatparticle.config;

import com.nali.chatparticle.system.Reference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Config(modid = Reference.MOD_ID)
public class MyConfig
{
    @Config.Name("Position")
    public static final Position POSITION = new Position();
    @Config.Name("Size")
    public static final Size SIZE = new Size();
    @Config.Name("Color")
    public static final Color COLOR = new Color();
    @Config.Name("Misc")
    public static final Misc MISC = new Misc();

    public static class Position
    {
        @Config.Name("X")
        public float X;

        @Config.Name("Y")
        public float Y = 1.0F;

        @Config.Name("Z")
        public float Z;
    }

    public static class Size
    {
        @Config.Name("X")
        public float X = -0.05F;

        @Config.Name("Y")
        public float Y = -0.05F;

        @Config.Name("Z")
        public float Z = -0.05F;
    }

    public static class Color
    {
        @Config.Name("R")
        @Config.RangeInt(min = 0, max = 255)
        public int R = 255;

        @Config.Name("G")
        @Config.RangeInt(min = 0, max = 255)
        public int G = 255;

        @Config.Name("B")
        @Config.RangeInt(min = 0, max = 255)
        public int B = 255;

        @Config.Name("A")
        @Config.RangeInt(min = 0, max = 255)
        public int A = 255;

        @Config.Name("Rainbow")
        public boolean RAINBOW;
    }

    public static class Misc
    {
        @Config.Name("On First Person")
        @Config.Comment("Player can see own particle on first person.")
        public boolean ON_FIRST_PERSON = true;
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Side.CLIENT)
    public static class ConfigEvent
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(Reference.MOD_ID))
            {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
