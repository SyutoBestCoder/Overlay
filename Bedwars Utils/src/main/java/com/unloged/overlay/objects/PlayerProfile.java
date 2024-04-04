package com.unloged.overlay.objects;

import com.unloged.overlay.utilities.APIUtils;
import com.unloged.overlay.utilities.PlayerUtils;

public class PlayerProfile {
    public String name;
    public String uuid;
    public boolean error;
    public boolean nicked;
    public PlayerStats stats;
    public String rank;
    public int stars;
    public int ws;
    public double fkdr;
    public long lastLogin;
    public boolean invalidKey;
    public boolean rateLimited;
    public String tags;

    public PlayerProfile(String uuid, String apiKey) {
        this.tags = "";
        this.uuid = uuid;
        this.stats = APIUtils.getStats(uuid, apiKey);
        if (this.stats == null) {
            this.error = true;
            return;
        }
        if (this.stats.invalidKey) {
            this.invalidKey = true;
            return;
        }
        if (this.stats.rateLimited) {
            this.rateLimited = true;
            return;
        }
        this.nicked = this.stats.nicked;
        if (this.nicked) {
            return;
        }
        if (!this.stats.name.isEmpty()) {
            this.name = this.stats.name;
        }
        if (!this.stats.userLanguage.equalsIgnoreCase("ENGLISH") && !this.stats.userLanguage.isEmpty()) {
            tags += "§3L";
        }
        if (Math.abs(System.currentTimeMillis() - this.stats.firstLogin) < 172800000) {
            tags += "§6F";
        }
        if (this.stats.lastLogout > 0 && Math.abs(System.currentTimeMillis() - this.stats.lastLogout) > 1210000000) {
            tags += "§6T";
        }
        if (this.stats.overall_final_deaths == 0 || this.stats.overall_losses == 0) {
            tags += "§5Z";
        }
        if (this.stats.duels_wlr > 5) {
            tags += "§fD";
        }
        if (this.stats.skywars_level > 20 || this.stats.skywars_kdr > 4 || this.stats.skywars_wlr > 4) {
            tags += "§fS";
        }
        if (this.stats.party) {
            tags += "§9P";
        }
        this.stars = this.stats.stars;
        this.fkdr = this.stats.overall_fkdr;
        this.fkdr = PlayerUtils.round(this.fkdr, 1);
        this.ws = this.stats.bedwars_ws;
        this.lastLogin = this.stats.lastLogin;
        this.rank = this.stats.rank;
    }
}