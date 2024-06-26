package com.nali.chatparticle;

import com.nali.chatparticle.system.Reference;
import com.nali.list.messages.ChatparticleClientMessage;
import com.nali.networks.NetworksRegistry;
import com.nali.system.bytes.BytesWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;

import static com.nali.chatparticle.particle.ChatParticle.CHATPARTICLE_SET;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ChatParticle
{
    @Instance
    public static ChatParticle I;
//    public static Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    @SubscribeEvent
    public static void onServerChatEvent(ServerChatEvent event)
    {
        EntityPlayerMP entityplayermp = event.getPlayer();
        String string = event.getMessage();
        byte[] string_byte_array = string.getBytes();
        byte[] byte_array = new byte[4 + string_byte_array.length];
        BytesWriter.set(byte_array, entityplayermp.getEntityId(), 0);
        System.arraycopy(string_byte_array, 0, byte_array, 4, string_byte_array.length);
        NetworksRegistry.I.sendToAll(new ChatparticleClientMessage(byte_array));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRenderWorldLastEvent(RenderWorldLastEvent event)
    {
        for (com.nali.chatparticle.particle.ChatParticle chatparticle : new HashSet<>(CHATPARTICLE_SET))
        {
            chatparticle.render(Minecraft.getMinecraft().player, event.getPartialTicks());
        }
    }
}
