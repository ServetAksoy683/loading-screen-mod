package com.animintro.mixin;

import com.animintro.ui.StyledButtons;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.LockButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClickableWidget.class)
public abstract class ClickableWidgetMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true, require = 0)
    private void sclient$render(DrawContext ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            ClickableWidget self = (ClickableWidget) (Object) this;
            if (!(self instanceof PressableWidget)) return;
            if (self instanceof TexturedButtonWidget || self instanceof LockButtonWidget || self instanceof CheckboxWidget) return;
            if (!self.visible) return;
            if (self.getMessage().getString().isEmpty()) return;
            StyledButtons.draw(ctx, self, mouseX, mouseY);
            ci.cancel();
        } catch (Throwable ignored) {
        }
    }
}
