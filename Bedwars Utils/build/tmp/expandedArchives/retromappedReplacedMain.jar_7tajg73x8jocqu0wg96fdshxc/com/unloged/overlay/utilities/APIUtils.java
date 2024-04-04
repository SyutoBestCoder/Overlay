package com.unloged.overlay.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unloged.overlay.objects.PlayerStats;

public class APIUtils {
    public static PlayerStats getStats(String uuid, String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return new PlayerStats(false, true, false, false);
        }
        String connection = NetworkUtils.newConnection("https://api.hypixel.net/player?key=" + apiKey + "&uuid=" + uuid);
        if (connection.isEmpty()) {
            return null;
        } else if (connection.equals("{\"success\":true,\"player\":null}")) {
            return new PlayerStats(true, false, false, false);
        } else if (connection.equals("{ \"success\": false, \"cause\": \"Key throttle\", \"throttle\": true, \"global\": true }")) {
            return new PlayerStats(false, false, true, false);
        } else if (connection.equals("{ \"success\": false, \"cause\": \"Invalid API key\" }")) {
            return new PlayerStats(false, true, false, false);
        } else {
            JsonObject profile;
            long lastLogin;
            long firstLogin;
            long lastLogout;
            JsonObject stats;
            JsonObject ach;
            JsonObject bw;
            try {
                profile = getStringAsJson(connection).getAsJsonObject("player");
                lastLogin = profile.has("lastLogin") ? profile.get("lastLogin").getAsLong() : -201;
                firstLogin = profile.has("firstLogin") ? profile.get("firstLogin").getAsLong() : -50;
                lastLogout = profile.has("lastLogout") ? profile.get("lastLogout").getAsLong() : -70;
                stats = profile.getAsJsonObject("stats");
                ach = profile.getAsJsonObject("achievements");
                bw = stats.getAsJsonObject("Bedwars");
            } catch (NullPointerException var47) {
                return new PlayerStats(false, false, false, true);
            }

            JsonObject duels = null;
            JsonObject sw = null;
            try {
                sw = stats.getAsJsonObject("SkyWars");
                duels = stats.getAsJsonObject("Duels");
            } catch (NullPointerException var47) {
            }

            String name = "";

            try {
                name = profile.get("displayname").getAsString();
            } catch (NullPointerException var46) {
            }

            boolean party = false;

            try {
                party = profile.get("channel").getAsString().equals("PARTY");
            } catch (NullPointerException var45) {
            }

            String userLanguage = "";
            try {
                userLanguage = profile.get("userLanguage").getAsString();
            } catch (NullPointerException var46) {
            }
            int stars = getValue(ach, "bedwars_level");
            int overall_final_kills = getValue(bw, "final_kills_bedwars");
            int bedwars_ws = getValue(bw, "winstreak");
            int overall_final_deaths = getValue(bw, "final_deaths_bedwars");
            int losses_bedwars = getValue(bw, "losses_bedwars");
            int skywars_level = getValue(sw, "skywars_you_re_a_star");
            int skywars_overall_kills = getValue(sw, "kills");
            int skywars_overall_deaths = getValue(sw, "deaths");
            int skywars_overall_wins = getValue(sw, "wins");
            int skywars_overall_losses = getValue(sw, "losses");
            int duels_losses = getValue(duels, "losses");
            int duels_wins = getValue(duels, "wins");

            String rank = "";

            try {
                if (profile.has("newPackageRank") && profile.get("monthlyPackageRank").getAsString().equals("SUPERSTAR")) {
                    rank = "SUPERSTAR";
                }
                else {
                    rank = profile.get("newPackageRank").getAsString();
                }
            } catch (NullPointerException var48) {
            }

            return new PlayerStats(name, party, stars, overall_final_kills, bedwars_ws, overall_final_deaths, lastLogin, firstLogin, lastLogout, losses_bedwars, skywars_level, skywars_overall_kills, skywars_overall_deaths, skywars_overall_wins, skywars_overall_losses, duels_wins, duels_losses, userLanguage, rank);
        }
    }

    public static JsonObject getStringAsJson(String text) {
        return (new JsonParser()).parse(text).getAsJsonObject();
    }

    public static int getValue(JsonObject type, String member) {
        try {
            return type.get(member).getAsInt();
        } catch (NullPointerException var3) {
            return 0;
        }
    }
}