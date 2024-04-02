package com.nali.list.handlers;

import com.nali.list.messages.ChatparticleClientMessage;
import com.nali.list.particle.ChatParticle;
import com.nali.system.bytes.BytesReader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static com.nali.chatparticle.particle.ChatParticle.ID_TEXT_MAP;

public class ChatparticleClientHandler implements IMessageHandler<ChatparticleClientMessage, IMessage>
{
    @Override
    public IMessage onMessage(ChatparticleClientMessage chatparticleclientmessage, MessageContext messagecontext)
    {
        int id = BytesReader.getInt(chatparticleclientmessage.data, 0);
        String string = new String(chatparticleclientmessage.data, 4, chatparticleclientmessage.data.length - 4);
        ID_TEXT_MAP.put(id, string);
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(id);
        if (entity != null)
        {
            Minecraft.getMinecraft().world.spawnParticle(ChatParticle.ENUMPARTICLETYPES, entity.posX, entity.posY + entity.height + 1.0F, entity.posZ, 0.0D, 0.0D, 0.0D, id, string.length());
        }
        return null;
    }
}