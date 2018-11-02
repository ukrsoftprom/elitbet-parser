package com.elitbet.parser;

import com.elitbet.parser.model.DataObject;


import java.util.List;

public interface Parser extends Runnable{
    List<DataObject> getDataObjects();
}
