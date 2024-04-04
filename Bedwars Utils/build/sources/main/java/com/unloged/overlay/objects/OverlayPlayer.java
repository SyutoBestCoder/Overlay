package com.unloged.overlay.objects;

import com.unloged.overlay.mods.impl.BedwarsOverlay;

public class OverlayPlayer
{
    public PlayerProfile playerProfile;
    public String displayName = "";
    public String starStr = "";
    public String fkdrStr = "";
    public String sessionStr = "";
    public String wsStr = "";
    public boolean nicked;
    public boolean loaded;
    public double score;
    public String tags = "";
    public int encounters = 1;
    public String rankColor = "";

    public boolean isLoaded() {
        return this.loaded;
    }

    public void finishedLoading() {
        this.loaded = true;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPlayerProfile(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
        this.starStr = BedwarsOverlay.checkData(playerProfile, BedwarsOverlay.getStarStr(playerProfile.stars));
        this.fkdrStr = BedwarsOverlay.checkData(playerProfile, BedwarsOverlay.getFKDRStr(playerProfile.fkdr));
        this.sessionStr = BedwarsOverlay.checkData(playerProfile, BedwarsOverlay.getSessionStr(playerProfile.lastLogin));
        this.wsStr = BedwarsOverlay.checkData(playerProfile, BedwarsOverlay.getwsStr(playerProfile.ws));
        this.score = playerProfile.stars * playerProfile.fkdr;
        this.tags = playerProfile.tags + this.tags;
        this.rankColor = BedwarsOverlay.toColor(this.playerProfile.rank);
    }

    public boolean isNicked() {
        return this.nicked;
    }

    public void setNicked(boolean nicked) {
        this.nicked = nicked;
    }

    public String getStarStr() {
        return this.starStr;
    }

    public String getFKDRStr() {
        return this.fkdrStr;
    }

    public double getScore() {
        return this.score;
    }

    public String getSessionStr() {
        return this.sessionStr;
    }

    public String getwsStr() {

        if (wsStr == "§4E") {
            return "-1";
        }
        return this.wsStr;
    }

    public String getEncountersStr() {
        return BedwarsOverlay.getEncountersStr(encounters);
    }

    public void setEncounters(int encounters) {
        this.encounters = encounters;
    }
}
