package com.elitbet.parser.impl;

import com.elitbet.parser.Parser;
import com.elitbet.parser.impl.saver.Saver;
import com.elitbet.parser.model.DataObject;
import com.elitbet.parser.model.FootballMatch;
import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Document;
import io.webfolder.ui4j.api.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class FootballParserPinnacle  {
    private final static Logger logger = Logger.getLogger(FootballParserPinnacle.class.getName());
    int year = Year.now().getValue();
    private BrowserEngine browser = BrowserFactory.getWebKit();
    private List<DataObject> dataObjects = new LinkedList<>();
    private SimpleDateFormat dateFormat;
    private Saver saver;

    public FootballParserPinnacle() {
        Locale locale = new Locale("ru", "RU");
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        dateFormatSymbols.setWeekdays(new String[]{
                "Unused",
                "Sun",
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Sat",
        });

        dateFormat = new SimpleDateFormat("E d/M  h.m y z", dateFormatSymbols);
    }


    private String parseTournamentName(String tournamentTitleRaw) {
        int indexBetting = tournamentTitleRaw.toLowerCase().indexOf("betting");
        int indexOdds = tournamentTitleRaw.toLowerCase().indexOf("odds");
        String tournamentName = null;

        if (indexBetting == -1 && indexOdds == -1) {
            tournamentName = tournamentTitleRaw;
        } else if (indexBetting < indexOdds && indexBetting != -1) {
            tournamentName = tournamentTitleRaw.substring(0, indexBetting);
        } else if (indexOdds != -1) {
            tournamentName = tournamentTitleRaw.substring(0, indexOdds);
        } else {
            tournamentName = tournamentTitleRaw;
        }

        return tournamentName.trim();
    }

    private void parseTournament(String tournamentLink) throws ParseException {
        Page page = browser.navigate(tournamentLink);
        Document doc = page.getDocument();
        String tournamentTitleRaw = null;
        if (doc.getTitle().isPresent()) {
            tournamentTitleRaw = doc.getTitle().get();
        } else {
            tournamentTitleRaw = "Default league odds";
        }
        String tournamentName = parseTournamentName(tournamentTitleRaw);
        List<Element> dates = doc.queryAll("div[ng-repeat~='currentPeriod.dates']");
        if (dates.size() > 0) {
            for (Element date : dates) {
                String dateRaw = date.queryAll("span.date-highlight").get(0).getText().get();
                Element oddsData = date.queryAll("table.odds-data").get(0);
                List<Element> tbodies = oddsData.queryAll("tbody[ng-repeat~='event']");
                for (Element tbody : tbodies) {
                    try {
                        List<Element> trs = tbody.queryAll("tr");
                        Element homeRow = trs.get(0);
                        List<Element> homeCols = homeRow.queryAll("td");
                        String gameTime = homeCols.get(0).getText().get().trim();
                        String tempDateRaw = dateRaw + " " + gameTime + " " + year + " GMT-07:00";
                        Date dateEvent = dateFormat.parse(tempDateRaw);
                        System.out.println(dateEvent.toString());
                        String homeTeam = homeCols.get(1).getText().get().trim();
                        double homeCoefficient = Double.valueOf(homeCols.get(2).getText().get());
                        Element guestRow = trs.get(1);
                        List<Element> guestCols = guestRow.queryAll("td");
                        String guestTeam = guestCols.get(1).getText().get().trim();
                        double guestCoefficient = Double.valueOf(guestCols.get(2).getText().get());
                        Element drawRow = trs.get(2);
                        List<Element> drawCols = drawRow.queryAll("td");
                        double drawCoefficient = Double.valueOf(drawCols.get(2).getText().get());
                        FootballMatch footballMatch = new FootballMatch();
                        footballMatch.setTournamentName(tournamentName);
                        footballMatch.setHomeTeam(homeTeam);
                        footballMatch.setGuestTeam(guestTeam);
                        footballMatch.setHomeCoefficient(homeCoefficient);
                        footballMatch.setGuestCoefficient(guestCoefficient);
                        footballMatch.setDrawCoefficient(drawCoefficient);
                        footballMatch.setDate(dateEvent);
                        dataObjects.add(footballMatch);
                    } catch (NumberFormatException ex) {
                        logger.severe(ex.toString());
                    } catch (IndexOutOfBoundsException ex) {
                        logger.severe(ex.toString());
                    }
                }
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.info("Pinnacle impl interrupted " + e.toString());
        }
    }

    public void setSaver(Saver saver) {
        this.saver = saver;
    }


}
