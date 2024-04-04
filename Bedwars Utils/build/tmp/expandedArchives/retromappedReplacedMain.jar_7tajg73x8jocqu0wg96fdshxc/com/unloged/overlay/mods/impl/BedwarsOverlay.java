package com.unloged.overlay.mods.impl;

import com.mojang.authlib.GameProfile;
import com.unloged.overlay.Main;
import com.unloged.overlay.config.Config;
import com.unloged.overlay.mods.Mod;
import com.unloged.overlay.objects.OverlayPlayer;
import com.unloged.overlay.objects.PlayerProfile;
import com.unloged.overlay.utilities.PlayerUtils;
import com.unloged.overlay.utilities.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BedwarsOverlay extends Mod {
    public static boolean toggled;
    private static boolean canShow;
    public static Map<UUID, OverlayPlayer> playersInLobby = Collections.synchronizedMap(new LinkedHashMap<>());
    public static Map<String, Integer> encounters = new ConcurrentHashMap<>();
    public static List<String> apiKeys = new ArrayList<>();
    public static boolean checkPlayers;

    public static void onChatEvent() {
        checkPlayers = true;
    }

    public static void onRenderTick() {
        if (mc.field_71462_r != null) {
            return;
        }
        if (Config.heldDown) {
            if (!Keyboard.isKeyDown(Config.showBind)) {
                return;
            }
        } else {
            if (!toggled && Keyboard.isKeyDown(Config.showBind)) {
                canShow = !canShow;
                toggled = true;
            } else if (!Keyboard.isKeyDown(Config.showBind)) {
                toggled = false;
            }
            if (!canShow) {
                return;
            }
        }

        GlStateManager.func_179094_E();
        GlStateManager.func_179084_k();
        Gui.func_73734_a(0, 30, 324, 51 + ((playersInLobby.size() != 0) ? 4 : 0) + 18 * playersInLobby.size(), new Color(0, 0, 0, 125).getRGB());
        Gui.func_73734_a(0, 29, 324, 30, -1); //top line
        Gui.func_73734_a(0, 50, 324, 51, -1); //bottom line
        Gui.func_73734_a(323, 29, 324, 51, -1); //fill + side line
        mc.field_71466_p.func_78276_b("ign", 5, 36, RenderUtils.getChroma(2L));
        RenderUtils.drawCenteredString("star", 165, 36, RenderUtils.getChroma(2L, 400L));
        RenderUtils.drawCenteredString("fkdr", 200, 36, RenderUtils.getChroma(2L, 200L));
        RenderUtils.drawCenteredString("ws", 235, 36, RenderUtils.getChroma(2L, 600L));
        RenderUtils.drawCenteredString("ses", 270, 36, RenderUtils.getChroma(2L, 600L));
        RenderUtils.drawCenteredString("tag", 305, 36, RenderUtils.getChroma(2L, 600L));
        int interval = 1;
        sortByStats();
        for (Map.Entry<UUID, OverlayPlayer> entry : playersInLobby.entrySet()) {
            final int y = 40 + 18 * interval++;
            final OverlayPlayer player = entry.getValue();
            if (!player.isLoaded()) {
                mc.field_71466_p.func_78276_b(player.getEncountersStr() + player.getDisplayName() + "§e [NICK] ", 5, y, -1);
                RenderUtils.drawCenteredString("§7...", 165, y, -1); //star
                RenderUtils.drawCenteredString("§7...", 200, y, -1); //fkdr
                RenderUtils.drawCenteredString("§7...", 235, y, -1); //ws
                RenderUtils.drawCenteredString("§7...", 270, y, -1); //ses
                RenderUtils.drawCenteredString("§7...", 305, y, -1); //tag
            }
            else if (!player.isNicked()) {
                mc.field_71466_p.func_78276_b(player.getEncountersStr() + player.getDisplayName(), 5, y, -1);
                RenderUtils.drawCenteredString(player.getStarStr(), 165, y, -1);
                RenderUtils.drawCenteredString(player.getFKDRStr(), 200, y, -1);
                RenderUtils.drawCenteredString(player.getwsStr(), 235, y, -1);
                RenderUtils.drawCenteredString(player.getSessionStr(), 270, y, -1);
                RenderUtils.drawCenteredString(player.tags, 305, y, -1);
            }
            else if (player.isNicked()) {
                mc.field_71466_p.func_78276_b(player.getEncountersStr() + player.getDisplayName() + "§e [NICK] ", 5, y, -1);
                RenderUtils.drawCenteredString("§7-", 165, y, -1); //star
                RenderUtils.drawCenteredString("§7-", 200, y, -1); //fkdr
                RenderUtils.drawCenteredString("§7-", 235, y, -1); //ws
                RenderUtils.drawCenteredString("§7-", 270, y, -1); //ses
                RenderUtils.drawCenteredString("§7-", 305, y, -1); //tag
            }
        }
        GlStateManager.func_179121_F();
    }

    public static void onTick() {
        if (!checkPlayers && PlayerUtils.getBedwarsStatus() != 1) {
            return;
        }
        int keyIndex = -1;
        List<NetworkPlayerInfo> playerList = new ArrayList<>(mc.func_147114_u().func_175106_d());
        List<UUID> loadedTab = new ArrayList<>();
        Iterator<NetworkPlayerInfo> iterator = playerList.iterator();
        while (iterator.hasNext()) {
            NetworkPlayerInfo networkPlayer = iterator.next();
            loadedTab.add(networkPlayer.func_178845_a().getId());
        }
        Iterator<Map.Entry<UUID, OverlayPlayer>> it = playersInLobby.entrySet().iterator();
        while (it.hasNext()) {
            final UUID uuid = it.next().getKey();
            if (!loadedTab.contains(uuid)) {
                it.remove();
            }
        }

        for (NetworkPlayerInfo networkPlayer : mc.func_147114_u().func_175106_d()) {
            GameProfile profile = networkPlayer.func_178845_a();
            if ((!profile.getName().contains("§") || profile.getName().startsWith("§c")) && networkPlayer.func_178853_c() != 1) {
                continue;
            }
            String name = profile.getName();
            if (playersInLobby.containsKey(profile.getId())) {
                continue;
            }
            String uuid = profile.getId().toString().replace("-", "");

            OverlayPlayer player = new OverlayPlayer();
            player.setDisplayName(getPlayerName(networkPlayer));
            playersInLobby.put(profile.getId(), player);
            if (PlayerUtils.contains(Main.snipers, name.trim())) {
                player.tags += "§cS";
                PlayerUtils.sendMessage("&b" + name + " &7is a &csniper&7!");
                mc.field_71439_g.func_85030_a("note.pling", 1.0f, 1.0f);
            }
            if (PlayerUtils.contains(Main.cheaters, name.trim())) {
                player.tags += "§cC";
                PlayerUtils.sendMessage("&b" + name + " &7is a &ccheater&7!");
                mc.field_71439_g.func_85030_a("note.pling", 1.0f, 1.0f);
            }
            String hypixelKey = "";
            if (keyIndex == apiKeys.size() - 1) {
                keyIndex = 0;
            }
            else {
                keyIndex++;
            }
            if (!apiKeys.isEmpty()) {
                if (keyIndex < 0 || keyIndex >= apiKeys.size()) {
                    keyIndex = 0;
                }
                if (apiKeys.get(keyIndex).isEmpty()) {
                    keyIndex = 0;
                }
                hypixelKey = apiKeys.get(keyIndex);
            }
            if (hypixelKey.isEmpty()) {
                player.finishedLoading();
                continue;
            }
            String finalHypixelKey = hypixelKey;
            if (!checkPlayers) {
                if (encounters.containsKey(uuid)) {
                    int count = encounters.get(uuid);
                    encounters.put(uuid, ++count);
                    player.setEncounters(count);
                } else {
                    encounters.put(uuid, 1);
                }
            }
            Main.getExecutor().execute(() -> {
                PlayerProfile p = new PlayerProfile(uuid, finalHypixelKey);

                if (p.nicked) {
                    player.setNicked(true);
                }

                player.setPlayerProfile(p);
                if (PlayerUtils.getBedwarsStatus() == 2) {
                    player.setDisplayName(player.rankColor + p.stats.name);
                }
                else {
                    player.setDisplayName(getPlayerName(networkPlayer));
                }
                player.finishedLoading();
                playersInLobby.put(profile.getId(), player);
            });
        }
        checkPlayers = false;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
    {
        return networkPlayerInfoIn.func_178854_k() != null ? networkPlayerInfoIn.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a(networkPlayerInfoIn.func_178850_i(), networkPlayerInfoIn.func_178845_a().getName());
    }

    public static String getFKDRStr(final double fkdr) {
        String color = "§";
        if (fkdr >= 50.0) {
            color += "4";
        }
        else if (fkdr >= 10.0) {
            color += "c";
        }
        else if (fkdr >= 7.0) {
            color += "6";
        }
        else if (fkdr >= 4.0) {
            color += "e";
        }
        else if (fkdr >= 1.0) {
            color += "a";
        }
        else {
            color += "2";
        }
        return color + fkdr;
    }

    public static String toColor(String rank) {
        if (!rank.isEmpty()) {
            if (rank.startsWith("VIP")) {
                return "§a";
            }
            if (rank.startsWith("MVP")) {
                return "§b";
            }
            if (rank.equals("SUPERSTAR")) {
                return "§6";
            }
        }
        return "§7";
    }

    public static String getStarStr(final int star) {
        String color = "§";
        if (star >= 1000) {
            color += "c";
        }
        else if (star >= 900) {
            color += "5";
        }
        else if (star >= 800) {
            color += "9";
        }
        else if (star >= 700) {
            color += "d";
        }
        else if (star >= 600) {
            color += "4";
        }
        else if (star >= 500) {
            color += "3";
        }
        else if (star >= 400) {
            color += "2";
        }
        else if (star >= 300) {
            color += "b";
        }
        else if (star >= 200) {
            color += "6";
        }
        else if (star >= 100) {
            color += "f";
        }
        else {
            color += "7";
        }
        return color + star;
    }

    public static String getSessionStr(long lastLogin) {
        if (lastLogin == -201) {
            return "§cAPI";
        }

        long sessionTime = calculateSessionTime(lastLogin);
        String color = sessionTime >= 32400 ? "§4" : sessionTime >= 21600 ? "§c" : sessionTime >= 10800 ? "§6" : sessionTime >= 7560 ? "§e" : sessionTime >= 1260 ? "§a" : sessionTime >= 330 ? "§e" : sessionTime >= 150 ? "§c" : "§4";
        String parsedString = parseSessionTime(sessionTime);

        return color + parsedString;
    }


    public static String getwsStr(final int ws) {
        String color = "§";
        if (ws == -1) {
            return "§cAPI";
        }
        if (ws >= 50) {
            color += "4";
        }
        else if (ws >= 10) {
            color += "c";
        }
        else if (ws >= 7) {
            color += "6";
        }
        else if (ws >= 4) {
            color += "e";
        }
        else if (ws >= 1) {
            color += "a";
        }
        else {
            color += "2";
        }
        return color + ws;
    }

    public static String checkData(PlayerProfile playerProfile, String data) {
        if (playerProfile.invalidKey) {
            return "§4KEY";
        }
        if (playerProfile.rateLimited) {
            return "§cRL";
        }
        if (playerProfile.error) {
            return "§4E";
        }
        return data;
    }

    public static void sortByStats() {
        List<Map.Entry<UUID, OverlayPlayer>> entryList = new ArrayList<>(playersInLobby.entrySet());

        entryList.sort(
                Comparator.comparing((Map.Entry<UUID, OverlayPlayer> entry) -> entry.getValue().isNicked())
                        .thenComparing(entry -> entry.getValue().getScore())
                        .reversed()
        );

        playersInLobby.clear();

        entryList.forEach(entry -> playersInLobby.put(entry.getKey(), entry.getValue()));
    }

    private static long calculateSessionTime(long lastLogin) {
        return Math.abs(System.currentTimeMillis() - lastLogin) / 1000;
    }

    public static String getEncountersStr(int encounters) {
        String color = encounters == 1 ? "§a" : encounters == 2 ? "§e" : encounters == 3 ? "§6" : encounters > 4 ? "§c" : "§4";
        return "§7(" + color + encounters + "§7) ";
    }

    private static String parseSessionTime(final float sessionTime) {
        float newSessionTime = sessionTime / 60;
        if (newSessionTime >= 10) {
            newSessionTime = (float) PlayerUtils.round(newSessionTime, 0);
            return String.valueOf((int) newSessionTime);
        }
        else {
            newSessionTime = (float) PlayerUtils.round(newSessionTime, 1);
        }
        return String.valueOf(newSessionTime);
    }
}
