package com.animintro.mixin;

import com.animintro.IntroRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {
    @Shadow private long reloadCompleteTime;
    @Shadow private float progress;

    @Unique private long animintro$start = -1L;

    @Inject(method = "render", at = @At("RETURN"))
    private void animintro$render(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        long now = Util.getMeasuringTimeMs();
        if (animintro$start < 0) animintro$start = now;

        // Orijinal overlay ile aynı fade-out mantığı
        float g = reloadCompleteTime > -1L ? (now - reloadCompleteTime) / 1000.0F : -1.0F;
        float alpha = g >= 1.0F ? 1.0F - MathHelper.clamp(g - 1.0F, 0.0F, 1.0F) : 1.0F;

        IntroRenderer.render(ctx, (now - animintro$start) / 1000.0F, progress, alpha);
    }
}
