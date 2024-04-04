package com.unloged.overlay;

import com.unloged.overlay.utilities.PlayerUtils;
import com.unloged.overlay.mods.impl.BedwarsOverlay;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class BedwarsCommand extends CommandBase {

    @Override
    public String func_71517_b() {
        return "bwu";
    }

    @Override
    public String func_71518_a(ICommandSender sender) {
        return "/bwu";
    }

    @Override
    public void func_71515_b(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            Main.gui = true;
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("tags")) {
                PlayerUtils.sendLine();
                PlayerUtils.sendMessage("§ePlayer tags list");
                PlayerUtils.sendMessage(" §3L§7: Non-English language");
                PlayerUtils.sendMessage(" §6F§7: First login <2 days");
                PlayerUtils.sendMessage(" §6T§7: Last logout >14 days");
                PlayerUtils.sendMessage(" §5Z§7: No losses or no final deaths");
                PlayerUtils.sendMessage(" §fD§7: High Duels stats");
                PlayerUtils.sendMessage(" §fS§7: High SkyWars stats");
                PlayerUtils.sendMessage(" §cC§7: Player is added as a cheater");
                PlayerUtils.sendMessage(" §cS§7: Player is added as a sniper");
                PlayerUtils.sendMessage(" §9P§7: Party tag");
                PlayerUtils.sendLine();
            }
            else if (args[0].equalsIgnoreCase("reload")) {
                BedwarsOverlay.checkPlayers = true;
                BedwarsOverlay.playersInLobby.clear();
                PlayerUtils.sendLine();
                PlayerUtils.sendMessage(" §aReloaded players!");
                PlayerUtils.sendLine();
            }
        }
        else {
            PlayerUtils.sendMessage("§cInvalid command.");
        }
    }

    @Override
    public int func_82362_a() {
        return -1;
    }

    @Override
    public boolean func_71519_b(ICommandSender sender) {
        return true;
    }
}
