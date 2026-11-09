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
    @Unique private boolean animintro$pushed = false;

    @Unique
    private float animintro$time() {
        long now = Util.getMeasuringTimeMs();
        if (animintro$start < 0) animintro$start = now;
        return (now - animintro$start) / 1000.0F;
    }

    // Vanilla Mojang logosu çizilmeden önce: logoya "yaklaşıp yerine oturma" animasyonu
    @Inject(method = "render", at = @At("HEAD"))
    private void animintro$head(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        float t = animintro$time();
        long now = Util.getMeasuringTimeMs();
        boolean menuNotYetShown = reloadCompleteTime <= -1L || (now - reloadCompleteTime) < 1000L;
        animintro$pushed = false;
        if (menuNotYetShown) {
            try {
                IntroRenderer.pushZoom(ctx, t);
                animintro$pushed = true;
            } catch (Throwable ignored) {
            }
        }
    }

    // Vanilla çizimi bitince: karartmadan açılma + logo üzerinde parlama
    @Inject(method = "render", at = @At("RETURN"))
    private void animintro$return(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (animintro$pushed) {
            ctx.getMatrices().pop();
            animintro$pushed = false;
        }
        try {
            long now = Util.getMeasuringTimeMs();
            float g = reloadCompleteTime > -1L ? (now - reloadCompleteTime) / 1000.0F : -1.0F;
            float alpha = g >= 1.0F ? 1.0F - MathHelper.clamp(g - 1.0F, 0.0F, 1.0F) : 1.0F;
            IntroRenderer.overlay(ctx, animintro$time(), alpha);
        } catch (Throwable ignored) {
        }
    }

    // Vanilla loading bar yerine animasyonlu bar (bulunamazsa sessizce vanilla kalır)
    @Inject(method = "renderProgressBar", at = @At("HEAD"), cancellable = true, require = 0)
    private void animintro$bar(DrawContext ctx, int minX, int minY, int maxX, int maxY, float opacity, CallbackInfo ci) {
        try {
            IntroRenderer.bar(ctx, minX, minY, maxX, maxY, opacity, progress, animintro$time());
            ci.cancel();
        } catch (Throwable ignored) {
        }
    }
}
