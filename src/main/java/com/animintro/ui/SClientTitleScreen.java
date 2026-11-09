package com.animintro.ui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class SClientTitleScreen extends Screen {
    public SClientTitleScreen() {
        super(Text.literal("S-Client"));
    }

    private static Screen modsScreen(Screen parent) {
        try {
            Class<?> c = Class.forName("com.terraformersmc.modmenu.gui.ModsScreen");
            return (Screen) c.getConstructor(Screen.class).newInstance(parent);
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    protected void init() {
        int w = this.width, h = this.height;
        int bh = MathHelper.clamp((int) (h * 0.075f), 20, 40);
        boolean mods = FabricLoader.getInstance().isModLoaded("modmenu");
        int n = mods ? 4 : 3;
        int gap = Math.max(6, (int) (w * 0.016f));
        int bw = (int) Math.min(w * 0.215f, (w * 0.9f - gap * (n - 1)) / n);
        int total = n * bw + (n - 1) * gap;
        int x = (w - total) / 2;
        int y1 = (int) (h * 0.745f);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.singleplayer"),
                b -> this.client.setScreen(new SelectWorldScreen(this)))
                .dimensions(x, y1, bw, bh).build());
        x += bw + gap;
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.multiplayer"),
                b -> this.client.setScreen(new MultiplayerScreen(this)))
                .dimensions(x, y1, bw, bh).build());
        x += bw + gap;
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.online"),
                b -> this.client.setScreen(new RealmsMainScreen(this)))
                .dimensions(x, y1, bw, bh).build());
        if (mods) {
            x += bw + gap;
            this.addDrawableChild(ButtonWidget.builder(Text.translatable("modmenu.title"),
                    b -> {
                        Screen s = modsScreen(this);
                        if (s != null) this.client.setScreen(s);
                    })
                    .dimensions(x, y1, bw, bh).build());
        }

        int qw = (int) (w * 0.2f);
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.quit"),
                b -> this.client.scheduleStop())
                .dimensions((w - qw) / 2, y1 + bh + gap, qw, bh).build());

        int sw = (int) (w * 0.14f);
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("options.title"),
                b -> this.client.setScreen(new OptionsScreen(this, this.client.options)))
                .dimensions(w - sw - (int) (w * 0.012f), (int) (h * 0.035f), sw, bh).build());
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderBackground(ctx, mouseX, mouseY, delta);
        int w = this.width, h = this.height;

        // Vinyet
        ctx.fillGradient(0, 0, w, h, 0x55000000, 0xCC000000);

        // Logo
        float scale = Math.min(h * 0.0145f, w * 0.40f / 50f);
        Decor.logo(ctx, w / 2, (int) (h * 0.05f), scale);

        // Orta panel (ileride kayan fotoğraflar buraya gelecek)
        int px = (int) (w * 0.158f), py = (int) (h * 0.254f);
        int pw = (int) (w * 0.685f), ph = (int) (h * 0.405f);
        ctx.fillGradient(px, py, px + pw, py + ph, 0x90202830, 0x90101810);
        Decor.panelFrame(ctx, px, py, pw, ph);
        ctx.drawCenteredTextWithShadow(this.textRenderer, Text.literal("animasyon eklenecek"),
                w / 2, py + ph / 2 - 4, 0xFFFFFFFF);

        Decor.footer(ctx, w, h);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
