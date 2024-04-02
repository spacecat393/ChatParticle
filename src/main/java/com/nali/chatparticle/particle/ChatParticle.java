package com.nali.chatparticle.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static com.nali.system.opengl.memory.OpenGLCurrentMemory.*;

@SideOnly(Side.CLIENT)
public class ChatParticle extends Particle
{
    public static Set<ChatParticle> CHATPARTICLE_SET = new LinkedHashSet<>();
    public static Map<Integer, String> ID_TEXT_MAP = new WeakHashMap<>();
    public int id;
    public String string;

    public ChatParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... id)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.id = id[0];
        this.particleAge = -(id[1] * 20 - this.particleMaxAge);
        CHATPARTICLE_SET.add(this);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        Entity entity = this.world.getEntityByID(this.id);
        if (entity != null)
        {
            this.setPosition(entity.posX, entity.posY + entity.height + 1.0F, entity.posZ);
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
    }

    @Override
    public void setExpired()
    {
        super.setExpired();
        CHATPARTICLE_SET.remove(this);
    }

    public void render(Entity entityIn, float partialTicks)
    {
        Entity entity = this.world.getEntityByID(this.id);
        if (entity != null)
        {
            if (this.string != null && !this.string.equals(ID_TEXT_MAP.get(this.id)))
            {
                this.setExpired();
            }
            else
            {
                if (this.string == null)
                {
                    this.string = ID_TEXT_MAP.get(this.id);
                }

                Minecraft minecraft = Minecraft.getMinecraft();
                RenderManager rendermanager = minecraft.getRenderManager();
                FontRenderer fontrenderer = minecraft.fontRenderer;

                float size = (fontrenderer.getStringWidth(this.string) * 0.05F) / 2.0F;
                this.setSize(size, 0.2F);

                float px = (float)(entityIn.prevPosX + (entityIn.posX - entityIn.prevPosX) * (double)partialTicks - interpPosX);
                float py = (float)(entityIn.prevPosY + (entityIn.posY - entityIn.prevPosY) * (double)partialTicks - interpPosY);
                float pz = (float)(entityIn.prevPosZ + (entityIn.posZ - entityIn.prevPosZ) * (double)partialTicks - interpPosZ);
                float x = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
                float y = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
                float z = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
                GL11.glPushMatrix();

//                GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);

                GL_LIGHTING = GL11.glIsEnabled(GL11.GL_LIGHTING);
//                GL11.glDisable(GL11.GL_LIGHTING);
                GlStateManager.disableLighting();

                float lx = OpenGlHelper.lastBrightnessX;
                float ly = OpenGlHelper.lastBrightnessY;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

//                for (int i = 0; i < 8; ++i)
//                {
//                    GlStateManager.disableLight(i);
//                }

                OPENGL_FIXED_PIPE_FLOATBUFFER.limit(16);
                GL11.glGetFloat(GL11.GL_CURRENT_COLOR, OPENGL_FIXED_PIPE_FLOATBUFFER);
                GL_CURRENT_COLOR[0] = OPENGL_FIXED_PIPE_FLOATBUFFER.get(0);
                GL_CURRENT_COLOR[1] = OPENGL_FIXED_PIPE_FLOATBUFFER.get(1);
                GL_CURRENT_COLOR[2] = OPENGL_FIXED_PIPE_FLOATBUFFER.get(2);
                GL_CURRENT_COLOR[3] = OPENGL_FIXED_PIPE_FLOATBUFFER.get(3);

                GL_BLEND = GL11.glIsEnabled(GL11.GL_BLEND);
//                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.enableBlend();

                GL11.glGetInteger(GL20.GL_BLEND_EQUATION_RGB, OPENGL_INTBUFFER);
                GL_BLEND_EQUATION_RGB = OPENGL_INTBUFFER.get(0);
                GL11.glGetInteger(GL20.GL_BLEND_EQUATION_ALPHA, OPENGL_INTBUFFER);
                GL_BLEND_EQUATION_ALPHA = OPENGL_INTBUFFER.get(0);

                GL20.glBlendEquationSeparate(GL14.GL_FUNC_ADD, GL14.GL_FUNC_ADD);

                GL11.glGetInteger(GL14.GL_BLEND_SRC_RGB, OPENGL_INTBUFFER);
                GL_BLEND_SRC_RGB = OPENGL_INTBUFFER.get(0);
                GL11.glGetInteger(GL14.GL_BLEND_SRC_ALPHA, OPENGL_INTBUFFER);
                GL_BLEND_SRC_ALPHA = OPENGL_INTBUFFER.get(0);
                GL11.glGetInteger(GL14.GL_BLEND_DST_RGB, OPENGL_INTBUFFER);
                GL_BLEND_DST_RGB = OPENGL_INTBUFFER.get(0);
                GL11.glGetInteger(GL14.GL_BLEND_DST_ALPHA, OPENGL_INTBUFFER);
                GL_BLEND_DST_ALPHA = OPENGL_INTBUFFER.get(0);
                GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

                GL_CULL_FACE = GL11.glIsEnabled(GL11.GL_CULL_FACE);
//                GL11.glDisable(GL11.GL_CULL_FACE);
                GlStateManager.disableCull();

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

//                GL11.glColorMask(true, true, true, true);
//                GL11.glColorMask(false, false, false, false);

//                GlStateManager.color(1, 1, 1, 1);

//                GlStateManager.disableRescaleNormal();
//                RenderHelper.disableStandardItemLighting();
//                GlStateManager.disableLighting();
//                GlStateManager.disableDepth();
//                GlStateManager.disableColorMaterial();
//                GlStateManager.disableFog();

//                GL20.glUseProgram(0);

//                GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, OPENGL_INTBUFFER);
//                GL_TEXTURE_BINDING_2D = OPENGL_INTBUFFER.get(0);

                GL11.glTranslatef(x - px, y - py, z - pz);
//                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(rendermanager.playerViewY, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(rendermanager.playerViewX, rendermanager.options.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);
//                GL11.glTranslatef(-(x + size / 2.0F), -y, -z);
//                GL11.glTranslatef(x - px + size / 2.0F, y - py, z - pz);
                GL11.glTranslatef(size, 0, 0);
                GL11.glScalef(-0.05F, -0.05F, -0.05F);
                fontrenderer.drawStringWithShadow(this.string, 0, 0, 0xFFFFFFFF);
//                GL11.glColorMask(true, true, true, true);
//                fontrenderer.drawStringWithShadow(this.string, 0, 0, 0xFFFFFFFF);

//                GL11.glBindTexture(GL11.GL_TEXTURE_2D, GL_TEXTURE_BINDING_2D);

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lx, ly);

//                if (GL_CULL_FACE)
//                {
//                    GL11.glEnable(GL11.GL_CULL_FACE);
//                }
//                else
//                {
//                    GL11.glDisable(GL11.GL_CULL_FACE);
//                }
//
//                GL20.glBlendEquationSeparate(GL_BLEND_EQUATION_RGB, GL_BLEND_EQUATION_ALPHA);
//                GL14.glBlendFuncSeparate(GL_BLEND_SRC_RGB, GL_BLEND_DST_RGB, GL_BLEND_SRC_ALPHA, GL_BLEND_DST_ALPHA);
//
//                if (GL_LIGHTING)
//                {
//                    GL11.glEnable(GL11.GL_LIGHTING);
//                }
//                else
//                {
//                    GL11.glDisable(GL11.GL_LIGHTING);
//                }
//
//                GL11.glColor4f(GL_CURRENT_COLOR[0], GL_CURRENT_COLOR[1], GL_CURRENT_COLOR[2], GL_CURRENT_COLOR[3]);

//                GL11.glPopAttrib();

                GL11.glPopMatrix();
            }
        }
    }
}
