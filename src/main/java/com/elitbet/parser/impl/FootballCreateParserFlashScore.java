package com.elitbet.parser.impl;

import com.elitbet.parser.Parser;
import com.elitbet.parser.model.DataObject;
import com.elitbet.parser.model.FootballMatch;
import com.elitbet.parser.impl.saver.Saver;
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

public class FootballCreateParserFlashScore implements Parser {

    private final static Logger logger = Logger.getLogger(FootballCreateParserFlashScore.class.getName());

    private List<DataObject> dataObjects;
    private BrowserEngine browser = BrowserFactory.getWebKit();
    private Saver saver;
    private long waitAfterClick;
    private SimpleDateFormat simpleDateFormat;

    FootballCreateParserFlashScore(){
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

    @Override
    public void parse() throws InterruptedException, ParseException {
        Page page = browser.navigate("https://www.flashscore.com/");
        Thread.sleep(1000);
        Document doc = page.getDocument();
        Element oddsTab = doc.queryAll("li.ifmenu-odds.li4 a").get(0);
        String date = doc.queryAll(".today").get(0).getText().get();
        String dateBase = "2018 " + date + " ";
        oddsTab.click();
        Thread.sleep(waitAfterClick);//changing tab to 'odds'
        List<Element> tournamentTables = doc.queryAll(".odds-content table.soccer");
        System.out.println(tournamentTables.size());
        for(Element tournamentTable:tournamentTables){
            Element tournamentHead = tournamentTable.queryAll("thead tr").get(0);
            String tournamentName = tournamentHead.queryAll(".name").get(0).getText().get();

            List<Element> tournamentEvents = tournamentTable.queryAll("tbody tr");
            for(Element tournamentEvent:tournamentEvents){
                List<Element> tournamentEventColumns = tournamentEvent.queryAll("td");
                Element timeElement = tournamentEventColumns.get(1);
                Element teamHomeElement = tournamentEventColumns.get(2); //home team
                Element teamAwayElement = tournamentEventColumns.get(3);
                Element eventScoreElement = tournamentEventColumns.get(4);
                Element teamHomeCoefficientElement = tournamentEventColumns.get(5);
                Element drawCoefficientElement = tournamentEventColumns.get(6);
                Element teamAwayCoefficientElement = tournamentEventColumns.get(7);

                String time = timeElement.getText().get();
                String teamHome = teamHomeElement.getText().get().replace("\u00a0","");
                String teamAway = teamAwayElement.getText().get().replace("\u00a0","");
                double teamHomeCoefficient = 0;
                double drawCoefficient = 0;
                double teamAwayCoefficient = 0;
                try {
                    teamHomeCoefficient = Double.valueOf(teamHomeCoefficientElement.getText().get());
                    drawCoefficient = Double.valueOf(drawCoefficientElement.getText().get());
                    teamAwayCoefficient = Double.valueOf(teamAwayCoefficientElement.getText().get());
                }catch (NumberFormatException ex){
                    // empty coefficient
                    continue;
                }

                String dateTimeToFormat = dateBase + time;

                Date dateObject = simpleDateFormat.parse(dateTimeToFormat);
                FootballMatch footballMatch = new FootballMatch();
                footballMatch.setDate(dateObject);
                footballMatch.setTournamentName(tournamentName);
                footballMatch.setHomeTeam(teamHome);
                footballMatch.setGuestTeam(teamAway);
                footballMatch.setHomeCoefficient(teamHomeCoefficient);
                footballMatch.setDrawCoefficient(drawCoefficient);
                footballMatch.setGuestCoefficient(teamAwayCoefficient);
                dataObjects.add(footballMatch);
            }
        }
    }

    @Override
    public List<DataObject> getDataObjects() {
        return dataObjects;
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
        logger.info("Parsing ended");
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
