package com.elitbet.parser.model;

import com.elitbet.parser.annotations.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Logger;

@Data
public class FootballMatch extends DataObject {
    private final static Logger logger = Logger.getLogger(FootballMatch.class.getName());
    private String homeTeam;
    private String guestTeam;
    private double homeCoefficient;
    private double guestCoefficient;
    private double drawCoefficient;
    private Date date;

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

    public double getHomeCoefficient() {
        return homeCoefficient;
    }

    public void setHomeCoefficient(double homeCoefficient) {
        this.homeCoefficient = homeCoefficient;
    }

    public double getGuestCoefficient() {
        return guestCoefficient;
    }

    public void setGuestCoefficient(double guestCoefficient) {
        this.guestCoefficient = guestCoefficient;
    }

    public double getDrawCoefficient() {
        return drawCoefficient;
    }

    public void setDrawCoefficient(double drawCoefficient) {
        this.drawCoefficient = drawCoefficient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTimestamp(){
        return date.getTime();
    }
    //event/footballmatch/add?firstteam=Arsenal&secondteam=Chelsea&tournament=English Premier League&firstwincoefficient=2.0&secondwincoefficient=2.5&drawcoefficient=3.2
    @Override
    public String toURL() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.
                    append("/event/footballmatch/add?firstteam=").
                    append(URLEncoder.encode(String.valueOf(homeTeam), "UTF-8")).
                    append("&secondteam=").
                    append(URLEncoder.encode(String.valueOf(guestTeam), "UTF-8")).
                    append("&tournament=").
                    append(URLEncoder.encode(String.valueOf(tournamentName), "UTF-8")).
                    append("&firstwincoefficient=").
                    append(URLEncoder.encode(String.valueOf(homeCoefficient), "UTF-8")).
                    append("&secondwincoefficient=").
                    append(URLEncoder.encode(String.valueOf(guestCoefficient), "UTF-8")).
                    append("&drawcoefficient=").
                    append(URLEncoder.encode(String.valueOf(drawCoefficient), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = stringBuilder.toString();
        return url;

    }

    @Override
    public String toString() {
        return "FootballMatch{" +
                "homeTeam='" + homeTeam + '\'' +
                ", guestTeam='" + guestTeam + '\'' +
                ", homeCoefficient=" + homeCoefficient +
                ", guestCoefficient=" + guestCoefficient +
                ", drawCoefficient=" + drawCoefficient +
                ", date=" + date +
                ", tournamentName='" + tournamentName + '\'' +
                '}';
    }


}
