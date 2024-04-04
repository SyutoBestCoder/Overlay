package com.unloged.overlay;

import com.unloged.overlay.utilities.PlayerUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BlacklistCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "blacklist";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/blacklist";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            PlayerUtils.sendLine();
            PlayerUtils.sendMessage("§eBlacklist");
            PlayerUtils.sendMessage(" §b" + Main.cheaters.size() + " §7cheaters added");
            PlayerUtils.sendMessage(" §b" + Main.snipers.size() + " §7snipers added");
            PlayerUtils.sendLine();
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("refresh")) {
                Main.getExecutor().execute(() -> {
                    Main.loadBlacklists();
                    PlayerUtils.sendMessage("&7Added &b" + (Main.cheaters.size() + Main.snipers.size()) + " &7players to blacklist.");
                });
            }
            else if (args[0].equalsIgnoreCase("help")) {
                PlayerUtils.sendLine();
                PlayerUtils.sendMessage("§eBlacklist commands:");
                PlayerUtils.sendMessage("");
                PlayerUtils.sendMessage(" §3/bl add <ign> <type>§e: Adds ign to the specified blacklist type.");
                PlayerUtils.sendMessage(" §3/bl refresh§e: Refresh your blacklists.");
                PlayerUtils.sendLine();
            }
            else {
                PlayerUtils.sendMessage("§cInvalid command");
            }
        }
        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add")) {
                File fileType;
                if (args[2].equalsIgnoreCase("c") || args[2].equalsIgnoreCase("cheater")) {
                    if (Main.cheaters.contains(args[1])) {
                        PlayerUtils.sendMessage("§b" + args[1] + " §7is already blacklisted as a cheater.");
                        return;
                    }
                    fileType = Main.cheatersFile;
                }
                else if (args[2].equalsIgnoreCase("s") || args[2].equalsIgnoreCase("sniper")) {
                    if (Main.snipers.contains(args[1])) {
                        PlayerUtils.sendMessage("§b" + args[1] + " §7is already blacklisted as a sniper.");
                        return;
                    }
                    fileType = Main.snipersFile;
                }
                else {
                    PlayerUtils.sendMessage("§cInvalid blacklist type.");
                    return;
                }
                addToBlacklist(args[1], fileType);
                PlayerUtils.sendMessage("§7Added §b" + args[1] + " §7to §b" + fileType.getName() + "§7.");
                Main.getExecutor().execute(() -> {
                    Main.loadBlacklists();
                    PlayerUtils.sendMessage("&7Added &b" + (Main.cheaters.size() + Main.snipers.size()) + " &7players to blacklist.");
                });
            }
            else {
                PlayerUtils.sendMessage("§cIncorrect usage: §b/bl <ign> <type>");
            }
        }
        else {
            PlayerUtils.sendMessage("§cInvalid command.");
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("bl", "blacklist");
    }

    private void addToBlacklist(String name, File type) {
        try (FileWriter writer = new FileWriter(type, true)) {
            writer.write(name + "\n");
        } catch (IOException e) {
            PlayerUtils.sendMessage("§cError writing to blacklist: §c" + e.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}

