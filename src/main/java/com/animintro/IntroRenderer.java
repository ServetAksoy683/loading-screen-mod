package com.animintro;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public final class IntroRenderer {
    private static final String TITLE = "MINECRAFT";
    private static final String SUB = "JAVA EDITION";

    private static float smoothProgress = 0f;

    private static int argb(int a, int r, int g, int b) {
        a = MathHelper.clamp(a, 0, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static float easeOutBack(float x) {
        float c1 = 1.70158f, c3 = c1 + 1f;
        float p = x - 1f;
        return 1f + c3 * p * p * p + c1 * p * p;
    }

    private static void drawScaled(DrawContext ctx, TextRenderer tr, String s, float x, float y, float scale, int color) {
        if ((color >>> 24) < 8) return; // alpha ~0 olursa vanilla opak çizer, atla
        ctx.getMatrices().push();
        ctx.getMatrices().translate(x, y, 0);
        ctx.getMatrices().scale(scale, scale, 1f);
        ctx.drawText(tr, s, 0, 0, color, true);
        ctx.getMatrices().pop();
    }

    public static void render(DrawContext ctx, float t, float progress, float alpha) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        int w = ctx.getScaledWindowWidth();
        int h = ctx.getScaledWindowHeight();
        int a = (int) (alpha * 255f);

        // Arka plan
        ctx.fillGradient(0, 0, w, h, argb(a, 8, 8, 22), argb(a, 28, 14, 48));

        // Yükselen küp parçacıkları
        for (int i = 0; i < 45; i++) {
            float x = ((i * 7919) % 1000) / 1000f * w;
            float speed = 8f + (i * 31) % 22;
            int size = 2 + i % 4;
            float y = h - ((t * speed + i * 37f) % (h + 12f)) + 6f;
            int pa = (int) (a * 0.35f);
            int col = (i % 3 == 0) ? argb(pa, 255, 200, 80) : (i % 3 == 1 ? argb(pa, 90, 220, 120) : argb(pa, 120, 170, 255));
            ctx.fill((int) x, (int) y, (int) x + size, (int) y + size, col);
        }

        // Başlık: harf harf animasyon
        float scale = Math.min(6f, w * 0.8f / tr.getWidth(TITLE));
        float totalW = tr.getWidth(TITLE) * scale;
        float startX = (w - totalW) / 2f;
        float baseY = h * 0.34f;
        float cursor = startX;
        for (int i = 0; i < TITLE.length(); i++) {
            String ch = String.valueOf(TITLE.charAt(i));
            float delay = 0.25f + i * 0.14f;
            float p = MathHelper.clamp((t - delay) / 0.55f, 0f, 1f);
            float e = easeOutBack(p);
            float yOff = (1f - e) * -40f * scale / 6f;
            int rgb = MathHelper.hsvToRgb(((i / 9f) + t * 0.08f) % 1f, 0.35f, 1f);
            int col = ((int) (a * p) << 24) | (rgb & 0xFFFFFF);
            drawScaled(ctx, tr, ch, cursor, baseY + yOff, scale, col);
            cursor += tr.getWidth(ch) * scale;
        }

        // Parlama (shine) efekti başlığın üstünden geçer
        float sp = (t - 2.0f) / 0.9f;
        if (sp > 0f && sp < 1f) {
            float sx = startX - 40f + sp * (totalW + 80f);
            int sa = (int) (a * 0.22f);
            ctx.fill((int) sx, (int) baseY - 4, (int) sx + 18, (int) (baseY + 8 * scale + 4), argb(sa, 255, 255, 255));
        }

        // Alt başlık
        float subP = MathHelper.clamp((t - 1.7f) / 0.6f, 0f, 1f);
        float subScale = Math.max(1.2f, scale / 3f);
        float subW = tr.getWidth(SUB) * subScale;
        drawScaled(ctx, tr, SUB, (w - subW) / 2f, baseY + 8 * scale + 10f, subScale,
                argb((int) (a * subP), 255, 210, 90));

        // İlerleme çubuğu
        smoothProgress += (progress - smoothProgress) * 0.08f;
        int bw = (int) (w * 0.4f);
        int bx = (w - bw) / 2;
        int by = (int) (h * 0.82f);
        float barP = MathHelper.clamp((t - 0.8f) / 0.5f, 0f, 1f);
        int ba = (int) (a * barP);
        ctx.fill(bx - 2, by - 2, bx + bw + 2, by + 6, argb((int) (ba * 0.25f), 90, 220, 120)); // glow
        ctx.fill(bx, by, bx + bw, by + 4, argb((int) (ba * 0.5f), 20, 20, 30));
        int fillW = (int) (bw * MathHelper.clamp(smoothProgress, 0f, 1f));
        ctx.fill(bx, by, bx + fillW, by + 4, argb(ba, 90, 220, 120));
    }
}
