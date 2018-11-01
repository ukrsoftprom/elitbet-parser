package com.elitbet.parser.impl;

import com.elitbet.parser.Parser;
import com.elitbet.parser.annotations.Data;
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
import java.util.*;
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
    public List<DataObject> getDataObjects() {
        return null;
    }

    @Override
    public void run() {

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