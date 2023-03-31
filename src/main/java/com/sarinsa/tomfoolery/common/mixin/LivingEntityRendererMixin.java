package com.sarinsa.tomfoolery.common.mixin;

import com.sarinsa.tomfoolery.common.misc.mixin.ClientMixinHooks;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


//@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }


    /*
    @Shadow
    protected abstract float getFlipDegrees(T entity);

    @Redirect(method = "setupRotations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getFlipDegrees(Lnet/minecraft/world/entity/LivingEntity;)F"))
    public float replaceFlipDegrees(LivingEntityRenderer<T, M> livingRenderer, T entity) {
        return ClientMixinHooks.getRendererFlipDegrees(getFlipDegrees(entity));
    }

     */
}
