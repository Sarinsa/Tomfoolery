package com.sarinsa.dumb_ores.common.mixin;

import com.sarinsa.dumb_ores.common.misc.mixin.ClientMixinHooks;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingRenderer.class)
public abstract class LivingRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements IEntityRenderer<T, M> {

    protected LivingRendererMixin(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Shadow
    protected abstract float getFlipDegrees(T entity);

    @Redirect(method = "setupRotations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingRenderer;getFlipDegrees(Lnet/minecraft/entity/LivingEntity;)F"))
    public float replaceFlipDegrees(LivingRenderer<T, M> livingRenderer, T entity) {
        return ClientMixinHooks.getRendererFlipDegrees(this.getFlipDegrees(entity));
    }
}
