package com.elitbet.parser.model;

import java.util.Date;

public class FootballMatchResult extends DataObject {

    private String homeTeam;
    private String guestTeam;
    private int homeTeamGoals;
    private int guestTeamGoals;
    private Date date;

    public FootballMatchResult() {
    }

    @Override
    public String toURL() {
        return null;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public int getHomeTeamGoals() {
        return homeTeamGoals;
    }

    public void setHomeTeamGoals(int homeTeamGoals) {
        this.homeTeamGoals = homeTeamGoals;
    }

    public int getGuestTeamGoals() {
        return guestTeamGoals;
    }

    public void setGuestTeamGoals(int guestTeamGoals) {
        this.guestTeamGoals = guestTeamGoals;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FootballMatchResult{" +
                "homeTeam='" + homeTeam + '\'' +
                ", guestTeam='" + guestTeam + '\'' +
                ", homeTeamGoals=" + homeTeamGoals +
                ", guestTeamGoals=" + guestTeamGoals +
                ", date=" + date +
                ", tournamentName='" + tournamentName + '\'' +
                '}';
    }
}
