package com.animintro.mixin;

import com.animintro.ui.Decor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin {
    @Inject(method = "renderWidget", at = @At("TAIL"), require = 0)
    private void sclient$frame(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            if (MinecraftClient.getInstance().world != null) return;
            ClickableWidget self = (ClickableWidget) (Object) this;
            Decor.panelFrame(ctx, self.getX(), self.getY(), self.getWidth(), self.getHeight());
        } catch (Throwable ignored) {
        }
    }
}
