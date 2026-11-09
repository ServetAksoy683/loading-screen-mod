package com.animintro.mixin;

import com.animintro.ui.Decor;
import com.animintro.ui.SClientTitleScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(method = "renderBackground", at = @At("RETURN"), require = 0)
    private void sclient$decor(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            if (MinecraftClient.getInstance().world != null) return;
            Screen self = (Screen) (Object) this;
            if (self instanceof SClientTitleScreen) return;
            Decor.screenDecor(ctx, self.width, self.height);
        } catch (Throwable ignored) {
        }
    }
}
