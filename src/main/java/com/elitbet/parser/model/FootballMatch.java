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



    /**
     * http://localhost:8080/events/create?access_token=1&event_type=Football Match&id=rmtmd6&start_timestamp=12334545674&parameters=home_name:ManCity;away_name:ManUtd;&tournament=EPL&coefficients=1:1.43;X:4.45;2:10.9;
     */
    @Override
    public String toURL() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.
                    append("/events/create?access_token=").
                    append(URLEncoder.encode("1", "UTF-8")).
                    append("&start_timestamp=").
                    append(URLEncoder.encode(String.valueOf(getTimestamp()), "UTF-8")).
                    append("&parameters=home_name:").
                    append(URLEncoder.encode(String.valueOf(homeTeam), "UTF-8")).
                    append(";away_name:").
                    append(URLEncoder.encode(String.valueOf(guestTeam), "UTF-8")).
                    append("&tournament=").
                    append(URLEncoder.encode(String.valueOf(tournamentName), "UTF-8")).
                    append("&coefficients=1:").
                    append(URLEncoder.encode(String.valueOf(homeCoefficient), "UTF-8")).
                    append(";2:").
                    append(URLEncoder.encode(String.valueOf(guestCoefficient), "UTF-8")).
                    append(";X:").
                    append(URLEncoder.encode(String.valueOf(drawCoefficient), "UTF-8")).
                    append("&id=").
                    append(URLEncoder.encode(eventId,"UTF-8")).
                    append("&event_type=").
                    append(URLEncoder.encode("Football Match", "UTF-8"));
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
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
