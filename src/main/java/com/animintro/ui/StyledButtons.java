package com.animintro.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/** Altın çerçeveli, cam efektli buton çizimi + soldaki ikon. */
public final class StyledButtons {
    private static final Map<String, ItemStack> ICONS = new HashMap<>();

    private StyledButtons() {}

    private static ItemStack makeIcon(String key) {
        switch (key) {
            case "menu.singleplayer":
            case "selectWorld.select":
                return new ItemStack(Items.IRON_SWORD);
            case "menu.multiplayer":
                return new ItemStack(Items.PLAYER_HEAD);
            case "menu.online":
                return new ItemStack(Items.GOLDEN_HELMET);
            case "modmenu.title":
                return new ItemStack(Items.IRON_PICKAXE);
            case "menu.quit":
                return new ItemStack(Items.OAK_DOOR);
            case "options.title":
            case "menu.options":
                return new ItemStack(Items.COMPARATOR);
            case "gui.back":
            case "gui.done":
                return new ItemStack(Items.ARROW);
            case "selectWorld.create":
                return new ItemStack(Items.GRASS_BLOCK);
            case "selectWorld.edit":
                return new ItemStack(Items.WRITABLE_BOOK);
            case "selectWorld.delete":
            case "gui.cancel":
                return new ItemStack(Items.BARRIER);
            case "selectWorld.recreate":
                return new ItemStack(Items.COMPASS);
            default:
                return null;
        }
    }

    private static ItemStack icon(String key) {
        if (!ICONS.containsKey(key)) ICONS.put(key, makeIcon(key));
        return ICONS.get(key);
    }

    public static void draw(DrawContext ctx, ClickableWidget wd, int mx, int my) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        int x = wd.getX(), y = wd.getY(), w = wd.getWidth(), h = wd.getHeight();
        boolean active = wd.active;
        boolean hover = active && mx >= x && my >= y && mx < x + w && my < y + h;
        boolean focus = wd.isFocused();

        Text msg = wd.getMessage();
        String key = null;
        if (msg.getContent() instanceof TranslatableTextContent t) key = t.getKey();
        ItemStack icon = key == null ? null : icon(key);
        boolean primary = "selectWorld.select".equals(key) || "gui.done".equals(key);
        boolean upper = mc.currentScreen instanceof SClientTitleScreen;

        // Zemin
        int top, bottom;
        if (primary) {
            top = hover ? 0xEE6E5A28 : 0xEE5A4A20;
            bottom = hover ? 0xEE3A2E14 : 0xEE2E2410;
        } else {
            top = hover ? 0xE03C3C42 : 0xE02C2C30;
            bottom = hover ? 0xE01C1C20 : 0xE0141416;
        }
        int border = !active ? 0x66777777 : (hover || focus || primary ? 0xFFE8CC7A : 0xFFB8974A);

        ctx.fillGradient(x + 1, y + 1, x + w - 1, y + h - 1, top, bottom);
        ctx.fill(x + 1, y, x + w - 1, y + 1, border);
        ctx.fill(x + 1, y + h - 1, x + w - 1, y + h, border);
        ctx.fill(x, y + 1, x + 1, y + h - 1, border);
        ctx.fill(x + w - 1, y + 1, x + w, y + h - 1, border);
        ctx.fill(x + 2, y + 1, x + w - 2, y + 2, 0x28FFFFFF);               // üst parlama
        ctx.fill(x + 2, y + h, x + w - 1, y + h + 1, active ? 0x88C9A24A : 0x33777777); // altın alt çizgi

        // İkon bölmesi
        int seg = 0;
        if (icon != null && w >= h * 2) {
            seg = h;
            ctx.fill(x + 1, y + 1, x + seg, y + h - 1, 0x55000000);
            ctx.fill(x + seg, y + 3, x + seg + 1, y + h - 3, 0x44C9A24A);
            ctx.drawItem(icon, x + (seg - 16) / 2, y + (h - 16) / 2);
            if (!active) ctx.fill(x + 1, y + 1, x + seg, y + h - 1, 0x99000000);
        }

        // Yazı
        String s = msg.getString();
        if (upper) s = s.toUpperCase(Locale.ROOT);
        int tw = tr.getWidth(s);
        int avail = w - seg - 8;
        float sc = 1f;
        if (tw > avail && tw > 0) sc = Math.max(0.5f, (float) avail / tw);
        float tx = x + seg + (w - seg) / 2f - tw * sc / 2f;
        float ty = y + (h - 8 * sc) / 2f + 0.5f;
        int color = !active ? 0xFF8A8A8A : (hover ? 0xFFFFE9A8 : 0xFFFFFFFF);
        ctx.getMatrices().push();
        ctx.getMatrices().translate(tx, ty, 0f);
        ctx.getMatrices().scale(sc, sc, 1f);
        ctx.drawText(tr, s, 0, 0, color, true);
        ctx.getMatrices().pop();
    }
}
