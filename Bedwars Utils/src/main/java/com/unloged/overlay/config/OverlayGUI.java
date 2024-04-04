package com.unloged.overlay.config;

import com.unloged.overlay.Main;
import com.unloged.overlay.mods.impl.BedwarsOverlay;
import com.unloged.overlay.utilities.PlayerUtils;
import com.unloged.overlay.utilities.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Objects;

public class OverlayGUI extends GuiScreen
{
    private GuiButtonExt enabled;
    private GuiButtonExt heldDown;
    private GuiButtonExt showBind;
    private GuiButtonExt refreshBlacklist;
    private GuiTextField hypixelAPI;
    private GuiTextField hypixelAPI2;
    private GuiTextField hypixelAPI3;
    private GuiTextField hypixelAPI4;
    private boolean binding;

    @Override
    public void initGui() {
        int y = this.height / 2 - 79;
        buttonList.add(enabled = new GuiButtonExt(0, this.width / 2 - 100, y, 200, 20, "Enabled: " + (Config.overlayEnabled ? "§aEnabled" : "§cDisabled")));
        buttonList.add(heldDown = new GuiButtonExt(1, this.width / 2 - 100, y + 27, 200, 20, "Held down: " + (Config.heldDown ? "§aEnabled" : "§cDisabled")));
        buttonList.add(showBind = new GuiButtonExt(2, this.width / 2 - 100, y + 54, 200, 20, "Show bind: " + (binding ? "..." : (Config.showBind == 0 ? "None" : ((Config.showBind >= 1000) ? RenderUtils.formatString(Mouse.getButtonName(Config.showBind - 1000)) : Keyboard.getKeyName(Config.showBind))))));
        hypixelAPI = new GuiTextField(3, fontRendererObj, this.width / 2 - 100, y + 81, 200, 20);
        hypixelAPI2 = new GuiTextField(4, fontRendererObj, this.width / 2 - 100, y + 108, 200, 20);
        hypixelAPI3 = new GuiTextField(5, fontRendererObj, this.width / 2 - 100, y + 135, 200, 20);
        hypixelAPI4 = new GuiTextField(6, fontRendererObj, this.width / 2 - 100, y + 162, 200, 20);
        buttonList.add(refreshBlacklist = new GuiButtonExt(7, this.width / 2 - 100, y + 189, 200, 20, "Refresh blacklist"));
        hypixelAPI.setMaxStringLength(1000);
        hypixelAPI2.setMaxStringLength(1000);
        hypixelAPI3.setMaxStringLength(1000);
        hypixelAPI4.setMaxStringLength(1000);
        hypixelAPI.setText(Objects.toString(Config.hypixelAPI_KEY, ""));
        hypixelAPI2.setText(Objects.toString(Config.hypixelAPI_KEY2, ""));
        hypixelAPI3.setText(Objects.toString(Config.hypixelAPI_KEY3, ""));
        hypixelAPI4.setText(Objects.toString(Config.hypixelAPI_KEY4, ""));
        updateScreen();
    }

    @Override
    public void drawScreen(int mx, int my, float delta) {
        drawDefaultBackground();
        hypixelAPI.drawTextBox();
        hypixelAPI2.drawTextBox();
        hypixelAPI3.drawTextBox();
        hypixelAPI4.drawTextBox();
        if (hypixelAPI.getText().isEmpty()) drawString(fontRendererObj, "Hypixel API Key", hypixelAPI.xPosition + 4, hypixelAPI.yPosition + (hypixelAPI.height - 8) / 2, 0xFF808080);
        if (hypixelAPI2.getText().isEmpty()) drawString(fontRendererObj, "Hypixel API Key 2", hypixelAPI2.xPosition + 4, hypixelAPI2.yPosition + (hypixelAPI2.height - 8) / 2, 0xFF808080);
        if (hypixelAPI3.getText().isEmpty()) drawString(fontRendererObj, "Hypixel API Key 3", hypixelAPI3.xPosition + 4, hypixelAPI3.yPosition + (hypixelAPI3.height - 8) / 2, 0xFF808080);
        if (hypixelAPI4.getText().isEmpty()) drawString(fontRendererObj, "Hypixel API Key 4", hypixelAPI4.xPosition + 4, hypixelAPI4.yPosition + (hypixelAPI4.height - 8) / 2, 0xFF808080);
        this.showBind.displayString = "Show bind: " + (binding ? "..." : (Config.showBind == 0 ? "None" : ((Config.showBind >= 1000) ? RenderUtils.formatString(Mouse.getButtonName(Config.showBind - 1000)) : Keyboard.getKeyName(Config.showBind))));
        drawCenteredString(mc.fontRendererObj, "Bedwars Overlay", this.width / 2, this.height / 2 - 95, -1);
        super.drawScreen(mx, my, delta);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button == this.enabled) {
            Config.overlayEnabled = !Config.overlayEnabled;
            this.enabled.displayString = "Enabled: " + (Config.overlayEnabled ? "§aEnabled" : "§cDisabled");
        } else if (button == this.heldDown) {
            Config.heldDown = !Config.heldDown;
            this.heldDown.displayString = "Held down: " + (Config.heldDown ? "§aEnabled" : "§cDisabled");
        } else if (button == this.showBind) {
            binding = true;
        } else if (button == this.refreshBlacklist) {
            Main.getExecutor().execute(() -> {
                Main.loadBlacklists();
                PlayerUtils.sendMessage("&7Added &b" + (Main.cheaters.size() + Main.snipers.size()) + " &7players to blacklist.");
            });
        }
    }

    @Override
    public void mouseClicked(int mx, int my, int btn) throws IOException {
        if (binding) {
            Config.showBind = btn + 1000;
            binding = false;
        }
        hypixelAPI.mouseClicked(mx, my, btn);
        hypixelAPI2.mouseClicked(mx, my, btn);
        hypixelAPI3.mouseClicked(mx, my, btn);
        hypixelAPI4.mouseClicked(mx, my, btn);
        super.mouseClicked(mx, my, btn);
    }

    @Override
    public void keyTyped(char t, int k) throws IOException {
        if (k == Keyboard.KEY_ESCAPE && !binding) {
            mc.displayGuiScreen(null);
            return;
        }
        if (binding) {
            if (k == Keyboard.KEY_ESCAPE) {
                Config.showBind = 0;
            } else {
                Config.showBind = k;
            }
            binding = false;
        }
        if (hypixelAPI.textboxKeyTyped(t, k) || hypixelAPI2.textboxKeyTyped(t, k) || hypixelAPI3.textboxKeyTyped(t, k) || hypixelAPI4.textboxKeyTyped(t, k)) return;

        super.keyTyped(t, k);
    }

    @Override
    public void updateScreen() {
        hypixelAPI.updateCursorCounter();
        hypixelAPI2.updateCursorCounter();
        hypixelAPI3.updateCursorCounter();
        hypixelAPI4.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Config.hypixelAPI_KEY = hypixelAPI.getText().trim().isEmpty() ? null : hypixelAPI.getText();
        Config.hypixelAPI_KEY2 = hypixelAPI2.getText().trim().isEmpty() ? null : hypixelAPI2.getText();
        Config.hypixelAPI_KEY3 = hypixelAPI3.getText().trim().isEmpty() ? null : hypixelAPI3.getText();
        Config.hypixelAPI_KEY4 = hypixelAPI4.getText().trim().isEmpty() ? null : hypixelAPI4.getText();
        Main.saveConfig();
        BedwarsOverlay.apiKeys.clear();
        if (Config.hypixelAPI_KEY != null && !Config.hypixelAPI_KEY.isEmpty()) {
            BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY);
        }
        if (Config.hypixelAPI_KEY2 != null && !Config.hypixelAPI_KEY2.isEmpty()) {
            BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY2);
        }
        if (Config.hypixelAPI_KEY3 != null && !Config.hypixelAPI_KEY3.isEmpty()) {
            BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY3);
        }
        if (Config.hypixelAPI_KEY4 != null && !Config.hypixelAPI_KEY4.isEmpty()) {
            BedwarsOverlay.apiKeys.add(Config.hypixelAPI_KEY4);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
