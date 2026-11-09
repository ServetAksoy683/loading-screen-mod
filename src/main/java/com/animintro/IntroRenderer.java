package com.animintro;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public final class IntroRenderer {
    private static float smooth = 0f;

    private static int argb(float a, int r, int g, int b) {
        int ai = MathHelper.clamp((int) a, 0, 255);
        return (ai << 24) | (r << 16) | (g << 8) | b;
    }

    /** Logo başta yakın gelir, 1.3 sn içinde yerine oturur. */
    private static float zoom(float t) {
        float p = MathHelper.clamp(t / 1.3f, 0f, 1f);
        float e = 1f - (1f - p) * (1f - p) * (1f - p);
        return 1f + 0.22f * (1f - e);
    }

    public static void pushZoom(DrawContext ctx, float t) {
        float s = zoom(t);
        float cx = ctx.getScaledWindowWidth() / 2f;
        float cy = ctx.getScaledWindowHeight() / 2f;
        MatrixStack m = ctx.getMatrices();
        m.push();
        m.translate(cx, cy, 0f);
        m.scale(s, s, 1f);
        m.translate(-cx, -cy, 0f);
    }

    public static void overlay(DrawContext ctx, float t, float alpha) {
        int w = ctx.getScaledWindowWidth();
        int h = ctx.getScaledWindowHeight();

        // Siyahtan açılma
        float f = 1f - MathHelper.clamp(t / 0.9f, 0f, 1f);
        if (f > 0f) {
            ctx.fill(0, 0, w, h, argb(f * 255f, 0, 0, 0));
        }

        // Logo üzerinden periyodik parlama (shine)
        float start = 1.4f;
        if (t > start) {
            float p = ((t - start) % 2.6f) / 1.1f;
            if (p < 1f) {
                float len = Math.min(w * 0.75f, h);
                int x1 = (int) (w / 2f - len / 2f);
                int x2 = (int) (x1 + len);
                int y1 = (int) (h / 2f - len * 0.15f);
                int y2 = (int) (h / 2f + len * 0.20f);
                float cx = x1 - len * 0.2f + p * len * 1.4f;
                int bw = Math.max(6, (int) (len * 0.08f));
                int col = argb(alpha * 70f, 255, 255, 255);
                ctx.enableScissor(x1, y1, x2, y2);
                for (int y = y1; y < y2; y += 4) {
                    int x = (int) (cx + (y - y1) * 0.35f);
                    ctx.fill(x, y, x + bw, y + 4, col);
                }
                ctx.disableScissor();
            }
        }
    }

    public static void bar(DrawContext ctx, int minX, int minY, int maxX, int maxY, float opacity, float progress, float t) {
        smooth += (progress - smooth) * 0.1f;
        float a = MathHelper.clamp(opacity, 0f, 1f) * 255f;

        // Dış çerçeve + koyu zemin
        ctx.fill(minX, minY, maxX, maxY, argb(a, 255, 255, 255));
        ctx.fill(minX + 2, minY + 2, maxX - 2, maxY - 2, argb(a * 0.0f + a, 160, 30, 40));

        int ix1 = minX + 4, iy1 = minY + 4, ix2 = maxX - 4, iy2 = maxY - 4;
        int fw = (int) ((ix2 - ix1) * MathHelper.clamp(smooth, 0f, 1f));
        if (fw < 2) return;

        // Dolgu
        ctx.fillGradient(ix1, iy1, ix1 + fw, iy2, argb(a, 255, 255, 255), argb(a, 255, 205, 205));

        // Akan çizgiler
        ctx.enableScissor(ix1, iy1, ix1 + fw, iy2);
        int step = 28;
        int off = (int) ((t * 70f) % step);
        for (int x = ix1 - step + off; x < ix1 + fw; x += step) {
            ctx.fill(x, iy1, x + 10, iy2, argb(a * 0.35f, 255, 120, 130));
        }
        ctx.disableScissor();

        // Ucu parlayan nabız
        float pulse = 0.5f + 0.5f * (float) Math.sin(t * 7f);
        int tip = ix1 + fw;
        ctx.fill(tip - 3, iy1 - 3, tip + 4, iy2 + 3, argb(a * (0.4f + 0.5f * pulse), 255, 245, 190));
    }
}
