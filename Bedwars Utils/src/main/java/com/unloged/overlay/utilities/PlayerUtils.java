package com.unloged.overlay.utilities;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.scoreboard.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerUtils implements MinecraftInstance
{
    public static boolean nullCheck() {
        return mc.thePlayer != null && mc.theWorld != null;
    }

    public static boolean isHypixel() {
        return !mc.isSingleplayer() && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel");
    }

    public static int getBedwarsStatus() {
        if (!nullCheck() || !isHypixel()) {
            return 0;
        }
        final Scoreboard scoreboard = mc.theWorld.getScoreboard();
        if (scoreboard == null) {
            return 0;
        }
        final ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null || !stripString(objective.getDisplayName()).contains("BED WARS")) {
            return 0;
        }
        int check = 0;
        for (final String line : getSidebarLines()) {
            final String strip = stripString(line);
            if (strip.contains("Mode:") && (strip.contains("Solo") || strip.contains("Doubles") || strip.contains("3v3") || strip.contains("4v4"))) {
                return 1;
            }
            if (strip.contains("Final Kills:")) {
                return 2;
            }
            if ((strip.contains("Red:") || strip.contains("Aqua:") || strip.contains("Gray:")) && ++check == 3) {
                return 2;
            }
        }
        return 0;
    }

    public static String stripString(final String s) {
        final char[] nonValidatedString = StringUtils.stripControlCodes(s).toCharArray();
        final StringBuilder validated = new StringBuilder();
        for (final char c : nonValidatedString) {
            if (c < '\u007f' && c > '\u0014') {
                validated.append(c);
            }
        }
        return validated.toString();
    }

    public static List<String> getSidebarLines() {
        final List<String> lines = new ArrayList<>();
        if (mc.theWorld == null) {
            return lines;
        }
        final Scoreboard scoreboard = mc.theWorld.getScoreboard();
        if (scoreboard == null) {
            return lines;
        }
        final ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) {
            return lines;
        }
        Collection<Score> scores = scoreboard.getSortedScores(objective);
        final List<Score> list = new ArrayList<>();
        for (final Score input : scores) {
            if (input != null && input.getPlayerName() != null && !input.getPlayerName().startsWith("#")) {
                list.add(input);
            }
        }
        if (list.size() > 15) {
            scores = (Collection<Score>) Lists.newArrayList(Iterables.skip((Iterable)list, scores.size() - 15));
        }
        else {
            scores = list;
        }
        for (final Score score : scores) {
            final ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }
        return lines;
    }

    public static void sendMessage(String message) {
        if (!nullCheck()) {
            return;
        }
        final String txt = replace("&7[&dBWU&7]&r " + message);
        mc.thePlayer.addChatMessage(new ChatComponentText(txt));
    }

    public static void sendLine() {
        sendMessage("&7&m-------------------------");
    }

    public static String replace(String text) {
        return text.replace("&", "§").replace("%and", "&");
    }

    public static double round(double number, int decimals) {
        if (decimals == 0) {
            return Math.round(number);
        }
        double power = Math.pow(10.0, decimals);
        return (double)Math.round(number * power) / power;
    }

    public static boolean contains(List<String> list, String target) {
        for (String string : list) {
            if (string.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}
