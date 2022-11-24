package com.sarinsa.tomfoolery.client.render.entity.cactus;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sarinsa.tomfoolery.common.entity.CactusBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Random;

public class CactusEntityRenderer<T extends CactusBlockEntity> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/cactus.png");
    private static final Random random = new Random();

    public CactusEntityRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(T cactusEntity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        BlockState state = Blocks.CACTUS.defaultBlockState();

        if (state.getRenderShape() == BlockRenderType.MODEL) {
            World world = cactusEntity.getCommandSenderWorld();

            if (state != world.getBlockState(cactusEntity.blockPosition()) && state.getRenderShape() != BlockRenderType.INVISIBLE) {
                matrixStack.pushPose();
                BlockPos pos = new BlockPos(cactusEntity.getX(), cactusEntity.getBoundingBox().maxY, cactusEntity.getZ());
                matrixStack.translate(-0.5D, 0.0D, -0.5D);
                BlockRendererDispatcher rendererDispatcher = Minecraft.getInstance().getBlockRenderer();

                for (RenderType type : RenderType.chunkBufferLayers()) {
                    if (RenderTypeLookup.canRenderInLayer(state, type)) {
                        ForgeHooksClient.setRenderLayer(type);
                        rendererDispatcher.getModelRenderer().tesselateBlock(world, rendererDispatcher.getBlockModel(state), state, pos, matrixStack, buffer.getBuffer(type), false, random, state.getSeed(pos), OverlayTexture.NO_OVERLAY);
                    }
                }
                ForgeHooksClient.setRenderLayer(null);
                matrixStack.popPose();
                super.render(cactusEntity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T cactusEntity) {
        return TEXTURE;
    }
}