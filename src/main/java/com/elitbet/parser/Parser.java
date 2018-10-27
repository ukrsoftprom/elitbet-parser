package com.elitbet.parser;

import com.elitbet.parser.model.DataObject;

import java.text.ParseException;
import java.util.List;

public interface Parser extends Runnable{
    void parse() throws InterruptedException, ParseException;

    List<DataObject> getDataObjects();
}
