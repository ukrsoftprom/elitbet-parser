package com.elitbet.parser.impl;

import com.elitbet.parser.Parser;
import com.elitbet.parser.model.DataObject;
import com.elitbet.parser.model.FootballMatch;
import com.elitbet.parser.impl.saver.Saver;
import com.elitbet.parser.model.FootballMatchResult;
import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Element;
import io.webfolder.ui4j.api.dom.Option;
import javafx.scene.control.Tab;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class FootballCreateParserFlashScore implements Parser {

    private final static Logger logger = Logger.getLogger(FootballCreateParserFlashScore.class.getName());

    private List<DataObject> dataObjects;
    private BrowserEngine browser = BrowserFactory.getWebKit();
    private Saver saver;
    private long waitAfterClick;
    private SimpleDateFormat simpleDateFormat;
    private String currentYear;
    private enum Tabs{
        ALL_GAMES, ODDS
    };

    Tabs currentTab = Tabs.ALL_GAMES;

    FootballCreateParserFlashScore() {
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

    private int[] parseGoals(String eventScoreRaw) throws ArrayIndexOutOfBoundsException {
        int[] goals = new int[2];
        try {
            System.out.println(eventScoreRaw);
            int index = eventScoreRaw.indexOf("(");
            String temp = eventScoreRaw;
            if (index > 0) {
                temp = eventScoreRaw.substring(0, index);
            }
            String[] goalsRaw = temp.split("-");
            goals[0] = Integer.valueOf(goalsRaw[0]);
            goals[1] = Integer.valueOf(goalsRaw[1]);
            return goals;
        } catch (Exception ex) {
            goals[0] = 0;
            goals[1] = 0;
            return goals;
        }
    }

    private String getTextFromElement(@NotNull Element element) {
        Optional<String> elementOptional = element.getText();
        if (elementOptional.isPresent()) {
            return elementOptional.get().replace("\u00a0", "");
        } else {
            return "";
        }
    }

    private String getAttributeFromElement(@NotNull Element element, String attibute) {
        Optional<String> attributeOptional = element.getAttribute(attibute);
        if (attributeOptional.isPresent()) {
            return attributeOptional.get();
        } else {
            return "";
        }
    }

    private void goToNextPage(@NotNull Page currentPage) {
        Document doc = currentPage.getDocument();
        Element oddsTab = doc.queryAll("li.ifmenu-odds.li4 a").get(0);
        Element tomorrowArrow = doc.queryAll(".tomorrow").get(0);
        tomorrowArrow.click();
        try {
            Thread.sleep(waitAfterClick);
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }

    private void goToPreviousPage(@NotNull Page currentPage) {
        Document doc = currentPage.getDocument();
        Element oddsTab = doc.queryAll("li.ifmenu-odds.li4 a").get(0);
        Element yesterdayArrow = doc.queryAll(".yesterday").get(0);
        yesterdayArrow.click();
        try {
            Thread.sleep(waitAfterClick);
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }

    private String getCurrentDayAndMonth(@NotNull Page page) {
        Document doc = page.getDocument();
        String date = doc.queryAll(".today").get(0).getText().get();
        String dateBase = currentYear + " " + date + " ";
        return dateBase;
    }

    private void chooseAllGamesTab(@NotNull Page page) {
        if(currentTab == Tabs.ALL_GAMES){
            return;
        }
        Document doc = page.getDocument();
        Element allGamesTab = doc.queryAll("li.ifmenu-all.li0 a").get(0);
        allGamesTab.click();
        try {
            Thread.sleep(waitAfterClick);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentTab = Tabs.ALL_GAMES;
    }

    private void chooseOddsTab(@NotNull Page page) {
        if(currentTab == Tabs.ODDS){
            return;
        }
        Document doc = page.getDocument();
        Element oddsTab = doc.queryAll("li.ifmenu-odds.li4 a").get(0);
        oddsTab.click();
        try {
            Thread.sleep(waitAfterClick);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentTab = Tabs.ODDS;
    }


    /**
     * @param page represents current page {@link Class#getName()}
     *             Parse all tables table.soccer from current type
     *             For All games tab uses
     * @see FootballCreateParserFlashScore#parseEventOnAllGamesTab(Element)
     */
    private void parseTablesOnCurrentTab(@NotNull Page page) {
        Document doc = page.getDocument();
        List<Element> tournamentsTables = doc.queryAll("table.soccer");
        for (Element tournamentTable : tournamentsTables) {

            Element tournamentNameElement = tournamentTable.queryAll("span.name").get(0);
            String tournamentName = getTextFromElement(tournamentNameElement);

            List<Element> tournamentEventElements = tournamentTable.queryAll("tbody tr");
            for (Element tournamentEventElement : tournamentEventElements) {

                ArrayList<Object> parsedEvent = null;
                switch (currentTab){
                    case ALL_GAMES:{
                        parsedEvent = parseEventOnAllGamesTab(tournamentEventElement);
                        break;
                    }
                    case ODDS:{
                        parsedEvent = parseEventOnOddsTab(tournamentEventElement);
                        break;
                    }
                    default:{
                        throw new TypeNotPresentException("Such tab type is not present", new Exception());
                    }
                }
                if(parsedEvent == null){
                    continue;
                }

                DataObject eventModel = (DataObject) parsedEvent.get(0);
                String time = (String) parsedEvent.get(1);

                String dateTimeToFormat = getCurrentDayAndMonth(page) + time;
                Date eventDate = null;
                try {
                    eventDate = simpleDateFormat.parse(dateTimeToFormat);
                } catch (ParseException e) {
                    logger.severe("Parse date error, dateTimeToFormat: " + dateTimeToFormat);
                }

                eventModel.setDate(eventDate);
                eventModel.setTournamentName(tournamentName);

                dataObjects.add(eventModel);
            }
        }
    }
    /**
     * @param event represents tr tag from http://flashscore.com/
     * @return ArrayList with DataObject event and String time
     */
    private ArrayList<Object> parseEventOnAllGamesTab(@NotNull Element event) {
        ArrayList<Object> result = new ArrayList<>(2);
        FootballMatchResult footballMatchResult = new FootballMatchResult();
        String tournamentEventId = getAttributeFromElement(event, "id");
        List<Element> tournamentEventColumns = event.queryAll("td");

        Element timeElement = tournamentEventColumns.get(1);
        Element statusElement = tournamentEventColumns.get(2);
        Element teamHomeElement = tournamentEventColumns.get(3);
        Element eventScoreElement = tournamentEventColumns.get(4);
        Element teamAwayElement = tournamentEventColumns.get(5);

        String eventStatus = getTextFromElement(statusElement);
        int[] eventScore = parseGoals(getTextFromElement(eventScoreElement));

        int teamHomeGoals = eventScore[0];
        int teamAwayGoals = eventScore[1];

        String time = getTextFromElement(timeElement);

        String teamHome = getTextFromElement(teamHomeElement);
        String teamAway = getTextFromElement(teamAwayElement);
        footballMatchResult.setHomeTeam(teamHome);
        footballMatchResult.setGuestTeam(teamAway);
        footballMatchResult.setEventId(tournamentEventId);
        footballMatchResult.setEventType("Football match");
        footballMatchResult.setStatus(eventStatus);
        footballMatchResult.setHomeTeamGoals(teamHomeGoals);
        footballMatchResult.setGuestTeamGoals(teamAwayGoals);

        result.add(footballMatchResult);
        result.add(time);

        return result;
    }
    private ArrayList<Object> parseEventOnOddsTab(@NotNull Element event){
        ArrayList<Object> result = new ArrayList<>(2);
        FootballMatch footballMatch = new FootballMatch();
        String tournamentEventId = getAttributeFromElement(event, "id");

        List<Element> tournamentEventColumns = event.queryAll("td");
        Element timeElement = tournamentEventColumns.get(1);
        Element teamHomeElement = tournamentEventColumns.get(2);
        Element teamAwayElement = tournamentEventColumns.get(3);
        Element eventScoreElement = tournamentEventColumns.get(4);
        Element teamHomeCoefficientElement = tournamentEventColumns.get(5);
        Element drawCoefficientElement = tournamentEventColumns.get(6);
        Element teamGuestCoefficientElement = tournamentEventColumns.get(7);

        double teamHomeCoefficient = 0;
        double drawCoefficient = 0;
        double teamGuestCoefficient = 0;
        try {
            teamHomeCoefficient = Double.valueOf(getTextFromElement(teamHomeCoefficientElement));
            drawCoefficient = Double.valueOf(getTextFromElement(drawCoefficientElement));
            teamGuestCoefficient = Double.valueOf(getTextFromElement(teamGuestCoefficientElement));
        } catch (NumberFormatException e) {
            logger.info(e.getMessage());
            return null;
        }

        String time = getTextFromElement(timeElement);
        String teamHome = getTextFromElement(teamHomeElement);
        String teamAway = getTextFromElement(teamAwayElement);

        footballMatch.setHomeTeam(teamHome);
        footballMatch.setGuestTeam(teamAway);
        footballMatch.setHomeCoefficient(teamHomeCoefficient);
        footballMatch.setDrawCoefficient(drawCoefficient);
        footballMatch.setGuestCoefficient(teamGuestCoefficient);
        footballMatch.setEventId(tournamentEventId);
        footballMatch.setEventType("Football match");

        result.add(footballMatch);
        result.add(time);

        return result;
    }

    @Override
    public void parse() throws InterruptedException, ParseException {
        Page page = browser.navigate("https://www.flashscore.com/");
        Thread.sleep(5000);

        Document doc = page.getDocument();
        Element oddsTab = doc.queryAll("li.ifmenu-odds.li4 a").get(0);
        String date = doc.queryAll(".today").get(0).getText().get();
        String dateBase = "2018 " + date + " ";
        oddsTab.click();
        Thread.sleep(waitAfterClick);//changing tab to 'odds'
        List<Element> tournamentTables = doc.queryAll(".odds-content table.soccer");
        System.out.println(tournamentTables.size());
        for (Element tournamentTable : tournamentTables) {
            Element tournamentHead = tournamentTable.queryAll("thead tr").get(0);
            String tournamentName = tournamentHead.queryAll(".name").get(0).getText().get();
            List<Element> tournamentEvents = tournamentTable.queryAll("tbody tr");
            for (Element tournamentEvent : tournamentEvents) {
                String tournamentEventId;
                Optional<String> tournamentEventIdOptional;
                if ((tournamentEventIdOptional = tournamentEvent.getAttribute("id")).isPresent()) {
                    tournamentEventId = tournamentEventIdOptional.get();
                } else {
                    tournamentEventId = "No id";
                }
                List<Element> tournamentEventColumns = tournamentEvent.queryAll("td");
                Element timeElement = tournamentEventColumns.get(1);
                Element teamHomeElement = tournamentEventColumns.get(2); //home team
                Element teamAwayElement = tournamentEventColumns.get(3);
                Element eventScoreElement = tournamentEventColumns.get(4);
                Element teamHomeCoefficientElement = tournamentEventColumns.get(5);
                Element drawCoefficientElement = tournamentEventColumns.get(6);
                Element teamAwayCoefficientElement = tournamentEventColumns.get(7);

                String time = timeElement.getText().get();
                String teamHome = teamHomeElement.getText().get().replace("\u00a0", "");
                String teamAway = teamAwayElement.getText().get().replace("\u00a0", "");
                double teamHomeCoefficient = 0;
                double drawCoefficient = 0;
                double teamAwayCoefficient = 0;
                try {
                    teamHomeCoefficient = Double.valueOf(teamHomeCoefficientElement.getText().get());
                    drawCoefficient = Double.valueOf(drawCoefficientElement.getText().get());
                    teamAwayCoefficient = Double.valueOf(teamAwayCoefficientElement.getText().get());
                } catch (NumberFormatException ex) {
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
                footballMatch.setEventId(tournamentEventId);
                footballMatch.setEventType("Football match");
                System.out.println(footballMatch);
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
        Page page = browser.navigate("https://www.flashscore.com/");
        page.show();
        chooseOddsTab(page);
        System.out.println(currentTab);
        parseTablesOnCurrentTab(page);
        dataObjects.forEach(System.out::println);
        chooseAllGamesTab(page);
        dataObjects.clear();
        parseTablesOnCurrentTab(page);
        dataObjects.forEach(System.out::println);

        /*try {
            //parse();
            *//*dataObjects.forEach(dataObject -> {
                System.out.println(dataObject.toURL());
            });
            saver.save(dataObjects);*//*
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("Parsing ended");*/
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

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }
}
