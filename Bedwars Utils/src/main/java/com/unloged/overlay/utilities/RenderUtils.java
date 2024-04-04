package com.unloged.overlay.utilities;

import java.awt.*;

public class RenderUtils implements MinecraftInstance {
    public static String formatString(String input) {
        return input.replaceAll("BUTTON(\\d+)", "BUTTON $1");
    }

    public static int getChroma(final long speed, final long... delay) {
        final long time = System.currentTimeMillis() + ((delay.length > 0) ? delay[0] : 0L);
        final int color = Color.getHSBColor(time % (15000L / speed) / (15000.0f / speed), 1.0f, 1.0f).getRGB();
        return color;
    }

    public static void drawCenteredString(final String text, final int x, final int y, final int color) {
        mc.fontRendererObj.drawStringWithShadow(text, (float)(x - mc.fontRendererObj.getStringWidth(text) / 2), (float)y, color);
    }

}
