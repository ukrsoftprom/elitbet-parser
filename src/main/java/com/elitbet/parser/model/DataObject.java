package com.elitbet.parser.model;

import java.util.Date;

public abstract class DataObject {
    protected String tournamentName;
    protected String eventId;
    protected String eventType;
    protected Date date;
    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public abstract String toURL();
    public long getTimestamp(){
        return date.getTime();
    }

}
