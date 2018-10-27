package com.elitbet.parser.model;

public abstract class DataObject {
    protected String tournamentName;

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public abstract String toURL();
}
