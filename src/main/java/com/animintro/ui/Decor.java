package com.animintro.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Ortak süslemeler: altın çerçeve, logo, sürüm yazısı. */
public final class Decor {
    public static final int GOLD = 0xFFC9A24A;
    public static final int GOLD_LIGHT = 0xFFE8CC7A;

    private Decor() {}

    /** Orta boy "S-Client" logosu. cx: merkez x, y: üst, scale: yazı ölçeği. */
    public static void logo(DrawContext ctx, int cx, int y, float scale) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        Text t = Text.literal("S-Client").formatted(Formatting.BOLD);
        int tw = tr.getWidth(t);
        ctx.getMatrices().push();
        ctx.getMatrices().translate(cx - tw * scale / 2f, y, 0f);
        ctx.getMatrices().scale(scale, scale, 1f);
        ctx.drawText(tr, t, 0, 0, 0xFFFFFFFF, true);
        ctx.getMatrices().pop();
    }

    public static void footer(DrawContext ctx, int w, int h) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        String s = "S-Client Build v2.0 | Minecraft " + mc.getGameVersion();
        ctx.drawCenteredTextWithShadow(tr, Text.literal(s), w / 2, h - 12, 0x88FFFFFF);
    }

    /** Altın köşe çizgileri ve orta işaretler. */
    public static void frame(DrawContext ctx, int w, int h) {
        int m = Math.max(6, (int) (h * 0.02f));
        int len = Math.max(20, (int) (h * 0.12f));
        int c = 0xAAC9A24A;
        int d = 0x44C9A24A;
        // köşeler
        ctx.fill(m, m, m + len, m + 1, c);
        ctx.fill(m, m, m + 1, m + len, c);
        ctx.fill(w - m - len, m, w - m, m + 1, c);
        ctx.fill(w - m - 1, m, w - m, m + len, c);
        ctx.fill(m, h - m - 1, m + len, h - m, c);
        ctx.fill(m, h - m - len, m + 1, h - m, c);
        ctx.fill(w - m - len, h - m - 1, w - m, h - m, c);
        ctx.fill(w - m - 1, h - m - len, w - m, h - m, c);
        // ince kenar çizgileri
        ctx.fill(m + len, m, w - m - len, m + 1, d);
        ctx.fill(m + len, h - m - 1, w - m - len, h - m, d);
        ctx.fill(m, m + len, m + 1, h - m - len, d);
        ctx.fill(w - m - 1, m + len, w - m, h - m - len, d);
        // orta işaretler
        int s = 2;
        ctx.fill(m - s, h / 2 - s, m + s, h / 2 + s, c);
        ctx.fill(w - m - s, h / 2 - s, w - m + s, h / 2 + s, c);
    }

    /** Çift çizgili altın panel çerçevesi. */
    public static void panelFrame(DrawContext ctx, int x, int y, int w, int h) {
        border(ctx, x - 3, y - 3, w + 6, h + 6, 0xFFE8D8A0);
        border(ctx, x - 1, y - 1, w + 2, h + 2, 0xFFB8974A);
    }

    private static void border(DrawContext ctx, int x, int y, int w, int h, int c) {
        ctx.fill(x + 1, y, x + w - 1, y + 1, c);
        ctx.fill(x + 1, y + h - 1, x + w - 1, y + h, c);
        ctx.fill(x, y + 1, x + 1, y + h - 1, c);
        ctx.fill(x + w - 1, y + 1, x + w, y + h - 1, c);
    }

    /** Menü ekranları için (ana menü hariç) tüm süsler. */
    public static void screenDecor(DrawContext ctx, int w, int h) {
        frame(ctx, w, h);
        logo(ctx, Math.max(40, w / 12), Math.max(10, (int) (h * 0.035f)), 1.6f);
        footer(ctx, w, h);
    }
}
