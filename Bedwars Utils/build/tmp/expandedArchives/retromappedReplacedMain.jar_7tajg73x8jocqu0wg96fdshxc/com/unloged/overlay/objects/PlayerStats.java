package com.unloged.overlay.objects;

public class PlayerStats
{
    public String name;
    public String userLanguage;
    public String rank;
    public boolean nicked;
    public boolean party;
    public boolean invalidKey;
    public boolean rateLimited;
    public boolean error;
    public int stars;
    public int overall_final_kills;
    public int bedwars_ws;
    public int overall_final_deaths;
    public double overall_fkdr;
    public long lastLogin;
    public long firstLogin;
    public long lastLogout;
    public int overall_losses;
    public int skywars_level;
    public int skywars_overall_kills;
    public int skywars_overall_wins;
    public int skywars_overall_deaths;
    public int skywars_overall_losses;
    public double skywars_wlr;
    public double skywars_kdr;
    public int duels_wins;
    public int duels_losses;
    public double duels_wlr;
    
    public PlayerStats(String name, boolean party, int stars, int overall_final_kills, int bedwars_ws, int overall_final_deaths, long lastLogin, long firstLogin, long lastLogout, int overall_losses, int skywars_level, int skywars_overall_kills, int skywars_overall_deaths, int skywars_overall_wins, int skywars_overall_losses, int duels_wins, int duels_losses, String userLanguage, String rank) {
        this.name = name;
        this.party = party;
        this.stars = stars;
        this.overall_final_kills = overall_final_kills;
        this.bedwars_ws = bedwars_ws;
        this.overall_final_deaths = overall_final_deaths;
        this.overall_losses = overall_losses;
        this.overall_fkdr = overall_final_deaths == 0 ? (double)overall_final_kills : this.round((double)overall_final_kills / (double)overall_final_deaths);
        this.lastLogin = lastLogin;
        this.firstLogin = firstLogin;
        this.lastLogout = lastLogout;
        this.skywars_level = skywars_level;
        this.skywars_overall_kills = skywars_overall_kills;
        this.skywars_overall_wins = skywars_overall_wins;
        this.skywars_overall_deaths = skywars_overall_deaths;
        this.skywars_overall_losses = skywars_overall_losses;
        this.skywars_kdr = skywars_overall_deaths == 0 ? (double)skywars_overall_kills : this.round((double)skywars_overall_kills / (double)skywars_overall_deaths);
        this.skywars_wlr = skywars_overall_losses == 0 ? (double)skywars_overall_wins : this.round((double)skywars_overall_wins / (double)skywars_overall_losses);
        this.duels_wins = duels_wins;
        this.duels_losses = duels_losses;
        this.duels_wlr = duels_losses == 0 ? (double)duels_wins : this.round((double)duels_wins / (double)duels_losses);
        this.userLanguage = userLanguage;
        this.rank = rank;
    }

    public PlayerStats(boolean nicked, boolean invalidKey, boolean  rateLimited, boolean error) {
        this.nicked = nicked;
        this.invalidKey = invalidKey;
        this.rateLimited = rateLimited;
        this.error = error;
        this.stars = 0;
    }
    
    private double round(double val) {
        return (double)Math.round(val * 100.0D) / 100.0D;
    }
}
 