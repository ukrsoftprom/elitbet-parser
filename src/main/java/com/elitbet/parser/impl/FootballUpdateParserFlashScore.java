package com.elitbet.parser.impl;

import com.elitbet.parser.Parser;
import com.elitbet.parser.impl.saver.Saver;
import com.elitbet.parser.model.DataObject;
import com.elitbet.parser.model.FootballMatch;
import com.elitbet.parser.model.FootballMatchResult;
import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Element;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class FootballUpdateParserFlashScore implements Parser {
    private final static Logger logger = Logger.getLogger(FootballCreateParserFlashScore.class.getName());

    private List<DataObject> dataObjects;
    private BrowserEngine browser = BrowserFactory.getWebKit();
    private Saver saver;
    private long waitAfterClick;
    private SimpleDateFormat simpleDateFormat;

    FootballUpdateParserFlashScore(){
        dataObjects = new ArrayList<>();
        Locale locale = new Locale("ru", "RU");
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        dateFormatSymbols.setWeekdays(new String[]{
                "Unused",
                "Su",
                "Mo",
                "Tu",
                "We",
                "Th",
                "Fr",
                "Sa",
        });

        simpleDateFormat = new SimpleDateFormat("y d/M E H:m", dateFormatSymbols);
    }
    private int[] parseGoals(String eventScoreRaw) throws ArrayIndexOutOfBoundsException{
        int [] goals = new int[2];
        try{
            System.out.println(eventScoreRaw);
            int index = eventScoreRaw.indexOf("(");
            String temp = eventScoreRaw;
            if(index > 0){
                temp = eventScoreRaw.substring(0,index);
            }
            String[] goalsRaw = temp.split("-");
            goals[0] = Integer.valueOf(goalsRaw[0]);
            goals[1] = Integer.valueOf(goalsRaw[1]);
            return goals;
        }catch (Exception ex){
            goals[0] = 0;
            goals[1] = 0;
            return goals;
        }
    }
    @Override
    public void parse() throws InterruptedException, ParseException {
        Page page = browser.navigate("https://www.flashscore.com/");
        Thread.sleep(5000);
        Document doc = page.getDocument();
        String date = doc.queryAll(".today").get(0).getText().get();
        String dateBase = "2018 " + date + " ";
        List<Element> tournamentTables = doc.queryAll(".table-main table.soccer");
        System.out.println(tournamentTables.size());
        for(Element tournamentTable:tournamentTables){
            Element tournamentHead = tournamentTable.queryAll("thead tr").get(0);
            String tournamentName = tournamentHead.queryAll(".name").get(0).getText().get();
            List<Element> tournamentEvents = tournamentTable.queryAll("tbody tr");
            for(Element tournamentEvent:tournamentEvents){
                List<Element> tournamentEventColumns = tournamentEvent.queryAll("td");
                Element timeElement = tournamentEventColumns.get(1);  //time
                Element eventStatusElement = tournamentEventColumns.get(2);
                String eventStatus = eventStatusElement.getText().get().replace("\u00a0","");
                Element teamHomeElement = tournamentEventColumns.get(3); //home team
                Element eventScoreElement = tournamentEventColumns.get(4);

                if(!eventScoreElement.getText().isPresent()){
                    continue;
                }
                String eventScoreRaw = eventScoreElement.getText().get().replace("\u00a0","");
                int [] goals;
                goals = parseGoals(eventScoreRaw);
                Element teamAwayElement = tournamentEventColumns.get(5);
                String time = timeElement.getText().get();
                String teamHome = teamHomeElement.getText().get().replace("\u00a0","");
                String teamAway = teamAwayElement.getText().get().replace("\u00a0","");
                String dateTimeToFormat = dateBase + time;
                Date dateObject = simpleDateFormat.parse(dateTimeToFormat);
                FootballMatchResult footballMatchResult = new FootballMatchResult();
                footballMatchResult.setDate(dateObject);
                footballMatchResult.setTournamentName(tournamentName);
                footballMatchResult.setHomeTeam(teamHome);
                footballMatchResult.setGuestTeam(teamAway);
                footballMatchResult.setHomeTeamGoals(goals[0]);
                footballMatchResult.setGuestTeamGoals(goals[1]);
                footballMatchResult.setStatus(eventStatus);
                dataObjects.add(footballMatchResult);
            }
        }
    }

    @Override
    public List<DataObject> getDataObjects() {
        return null;
    }

    @Override
    public void run() {
        try {
            parse();
            dataObjects.forEach(System.out::println);
            saver.save(dataObjects);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Saver getSaver() {
        return saver;
    }

    public void setSaver(Saver saver) {
        this.saver = saver;
    }

    public long getWaitAfterClick() {
        return waitAfterClick;
    }

    public void setWaitAfterClick(long waitAfterClick) {
        this.waitAfterClick = waitAfterClick;
    }
}