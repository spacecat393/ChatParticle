package com.nali.list.messages;

import com.nali.networks.NetworksMessage;
import net.minecraftforge.fml.relauncher.Side;

public class ChatparticleClientMessage extends NetworksMessage
{
    public static Side SIDE = Side.CLIENT;
    public ChatparticleClientMessage()
    {

    }

    public ChatparticleClientMessage(byte[] data)
    {
        this.data = data;
    }
}
