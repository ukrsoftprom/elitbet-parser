package com.elitbet.parser.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

public class FootballMatchResult extends DataObject {

    private String homeTeam;
    private String guestTeam;
    private int homeTeamGoals;
    private int guestTeamGoals;
    private Date date;
    private String status;
    public FootballMatchResult() {
    }

    @Override
    public String toURL() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.
                    append("access_token=").
                    append(URLEncoder.encode("mikola_lox", "UTF-8")).
                    append("&start_timestamp=").
                    append(URLEncoder.encode(String.valueOf(date.getTime()), "UTF-8")).
                    append("&first_name=").
                    append(URLEncoder.encode(homeTeam, "UTF-8")).
                    append("&second_name=").
                    append(URLEncoder.encode(guestTeam, "UTF-8")).
                    append("&tournament=").
                    append(URLEncoder.encode(tournamentName, "UTF-8")).
                    append("&first_goals=").
                    append(URLEncoder.encode(String.valueOf(homeTeamGoals), "UTF-8")).
                    append("&second_goals=").
                    append(URLEncoder.encode(String.valueOf(guestTeamGoals), "UTF-8")).
                    append("&status=").
                    append(URLEncoder.encode(status,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = stringBuilder.toString();
        return url;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FootballMatchResult{" +
                "homeTeam='" + homeTeam + '\'' +
                ", guestTeam='" + guestTeam + '\'' +
                ", homeTeamGoals=" + homeTeamGoals +
                ", guestTeamGoals=" + guestTeamGoals +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", tournamentName='" + tournamentName + '\'' +
                '}';
    }
}
