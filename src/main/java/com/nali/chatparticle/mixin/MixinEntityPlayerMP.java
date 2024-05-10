package com.nali.chatparticle.mixin;

import com.mojang.authlib.GameProfile;
import com.nali.list.messages.ChatparticleClientMessage;
import com.nali.networks.NetworksRegistry;
import com.nali.system.bytes.BytesWriter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer
{
    public MixinEntityPlayerMP(World worldIn, GameProfile gameProfileIn)
    {
        super(worldIn, gameProfileIn);
    }

    @Inject(method = "sendStatusMessage", at = @At(value = "HEAD"))
    private void nali_chatparticle_sendStatusMessage(ITextComponent chatComponent, boolean actionBar, CallbackInfo ci)
    {
        String string = chatComponent.getFormattedText();
        byte[] string_byte_array = string.getBytes();
        byte[] byte_array = new byte[4 + string_byte_array.length];
        BytesWriter.set(byte_array, this.getEntityId(), 0);
        System.arraycopy(string_byte_array, 0, byte_array, 4, string_byte_array.length);
        NetworksRegistry.I.sendToAll(new ChatparticleClientMessage(byte_array));
//        ((WorldServer)this.world).spawnParticle(ENUMPARTICLETYPES, this.posX, this.posY + this.height, this.posZ, 1, 0.0D, 0.0D, 0.0D, 0.0D, this.getEntityId(), string.length());
    }
}
