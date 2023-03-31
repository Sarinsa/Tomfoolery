package com.sarinsa.tomfoolery.client.render.entity.cactus;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.sarinsa.tomfoolery.common.entity.CactusBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.ModelData;

import javax.annotation.Nonnull;
import java.util.Random;

public class CactusEntityRenderer<T extends CactusBlockEntity> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/block/cactus.png");
    private final BlockRenderDispatcher dispatcher;


    public CactusEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.5F;
        dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(T entity, float p_114635_, float p_114636_, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        BlockState state = Blocks.CACTUS.defaultBlockState();

        if (state.getRenderShape() == RenderShape.MODEL) {
            Level level = entity.getLevel();

            if (state != level.getBlockState(entity.blockPosition()) && state.getRenderShape() != RenderShape.INVISIBLE) {
                poseStack.pushPose();
                BlockPos pos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                poseStack.translate(-0.5D, 0.0D, -0.5D);
                var model = dispatcher.getBlockModel(state);

                for (var renderType : model.getRenderTypes(state, RandomSource.create(state.getSeed(entity.getStartPos())), ModelData.EMPTY))
                    dispatcher.getModelRenderer().tesselateBlock(level, model, state, pos, poseStack, bufferSource.getBuffer(renderType), false, RandomSource.create(), state.getSeed(entity.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);

                poseStack.popPose();
                super.render(entity, p_114635_, p_114636_, poseStack, bufferSource, packedLight);
            }
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(T cactusEntity) {
        return TEXTURE;
    }
}