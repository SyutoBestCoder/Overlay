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
    public void func_73866_w_() {
        int y = this.field_146295_m / 2 - 79;
        field_146292_n.add(enabled = new GuiButtonExt(0, this.field_146294_l / 2 - 100, y, 200, 20, "Enabled: " + (Config.overlayEnabled ? "§aEnabled" : "§cDisabled")));
        field_146292_n.add(heldDown = new GuiButtonExt(1, this.field_146294_l / 2 - 100, y + 27, 200, 20, "Held down: " + (Config.heldDown ? "§aEnabled" : "§cDisabled")));
        field_146292_n.add(showBind = new GuiButtonExt(2, this.field_146294_l / 2 - 100, y + 54, 200, 20, "Show bind: " + (binding ? "..." : (Config.showBind == 0 ? "None" : ((Config.showBind >= 1000) ? RenderUtils.formatString(Mouse.getButtonName(Config.showBind - 1000)) : Keyboard.getKeyName(Config.showBind))))));
        hypixelAPI = new GuiTextField(3, field_146289_q, this.field_146294_l / 2 - 100, y + 81, 200, 20);
        hypixelAPI2 = new GuiTextField(4, field_146289_q, this.field_146294_l / 2 - 100, y + 108, 200, 20);
        hypixelAPI3 = new GuiTextField(5, field_146289_q, this.field_146294_l / 2 - 100, y + 135, 200, 20);
        hypixelAPI4 = new GuiTextField(6, field_146289_q, this.field_146294_l / 2 - 100, y + 162, 200, 20);
        field_146292_n.add(refreshBlacklist = new GuiButtonExt(7, this.field_146294_l / 2 - 100, y + 189, 200, 20, "Refresh blacklist"));
        hypixelAPI.func_146203_f(1000);
        hypixelAPI2.func_146203_f(1000);
        hypixelAPI3.func_146203_f(1000);
        hypixelAPI4.func_146203_f(1000);
        hypixelAPI.func_146180_a(Objects.toString(Config.hypixelAPI_KEY, ""));
        hypixelAPI2.func_146180_a(Objects.toString(Config.hypixelAPI_KEY2, ""));
        hypixelAPI3.func_146180_a(Objects.toString(Config.hypixelAPI_KEY3, ""));
        hypixelAPI4.func_146180_a(Objects.toString(Config.hypixelAPI_KEY4, ""));
        func_73876_c();
    }

    @Override
    public void func_73863_a(int mx, int my, float delta) {
        func_146276_q_();
        hypixelAPI.func_146194_f();
        hypixelAPI2.func_146194_f();
        hypixelAPI3.func_146194_f();
        hypixelAPI4.func_146194_f();
        if (hypixelAPI.func_146179_b().isEmpty()) func_73731_b(field_146289_q, "Hypixel API Key", hypixelAPI.field_146209_f + 4, hypixelAPI.field_146210_g + (hypixelAPI.field_146219_i - 8) / 2, 0xFF808080);
        if (hypixelAPI2.func_146179_b().isEmpty()) func_73731_b(field_146289_q, "Hypixel API Key 2", hypixelAPI2.field_146209_f + 4, hypixelAPI2.field_146210_g + (hypixelAPI2.field_146219_i - 8) / 2, 0xFF808080);
        if (hypixelAPI3.func_146179_b().isEmpty()) func_73731_b(field_146289_q, "Hypixel API Key 3", hypixelAPI3.field_146209_f + 4, hypixelAPI3.field_146210_g + (hypixelAPI3.field_146219_i - 8) / 2, 0xFF808080);
        if (hypixelAPI4.func_146179_b().isEmpty()) func_73731_b(field_146289_q, "Hypixel API Key 4", hypixelAPI4.field_146209_f + 4, hypixelAPI4.field_146210_g + (hypixelAPI4.field_146219_i - 8) / 2, 0xFF808080);
        this.showBind.field_146126_j = "Show bind: " + (binding ? "..." : (Config.showBind == 0 ? "None" : ((Config.showBind >= 1000) ? RenderUtils.formatString(Mouse.getButtonName(Config.showBind - 1000)) : Keyboard.getKeyName(Config.showBind))));
        func_73732_a(field_146297_k.field_71466_p, "Bedwars Overlay", this.field_146294_l / 2, this.field_146295_m / 2 - 95, -1);
        super.func_73863_a(mx, my, delta);
    }

    @Override
    public void func_146284_a(GuiButton button) {
        if (button == this.enabled) {
            Config.overlayEnabled = !Config.overlayEnabled;
            this.enabled.field_146126_j = "Enabled: " + (Config.overlayEnabled ? "§aEnabled" : "§cDisabled");
        } else if (button == this.heldDown) {
            Config.heldDown = !Config.heldDown;
            this.heldDown.field_146126_j = "Held down: " + (Config.heldDown ? "§aEnabled" : "§cDisabled");
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
    public void func_73864_a(int mx, int my, int btn) throws IOException {
        if (binding) {
            Config.showBind = btn + 1000;
            binding = false;
        }
        hypixelAPI.func_146192_a(mx, my, btn);
        hypixelAPI2.func_146192_a(mx, my, btn);
        hypixelAPI3.func_146192_a(mx, my, btn);
        hypixelAPI4.func_146192_a(mx, my, btn);
        super.func_73864_a(mx, my, btn);
    }

    @Override
    public void func_73869_a(char t, int k) throws IOException {
        if (k == Keyboard.KEY_ESCAPE && !binding) {
            field_146297_k.func_147108_a(null);
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
        if (hypixelAPI.func_146201_a(t, k) || hypixelAPI2.func_146201_a(t, k) || hypixelAPI3.func_146201_a(t, k) || hypixelAPI4.func_146201_a(t, k)) return;

        super.func_73869_a(t, k);
    }

    @Override
    public void func_73876_c() {
        hypixelAPI.func_146178_a();
        hypixelAPI2.func_146178_a();
        hypixelAPI3.func_146178_a();
        hypixelAPI4.func_146178_a();
        super.func_73876_c();
    }

    @Override
    public void func_146281_b() {
        Config.hypixelAPI_KEY = hypixelAPI.func_146179_b().trim().isEmpty() ? null : hypixelAPI.func_146179_b();
        Config.hypixelAPI_KEY2 = hypixelAPI2.func_146179_b().trim().isEmpty() ? null : hypixelAPI2.func_146179_b();
        Config.hypixelAPI_KEY3 = hypixelAPI3.func_146179_b().trim().isEmpty() ? null : hypixelAPI3.func_146179_b();
        Config.hypixelAPI_KEY4 = hypixelAPI4.func_146179_b().trim().isEmpty() ? null : hypixelAPI4.func_146179_b();
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
    public boolean func_73868_f() {
        return false;
    }
}
