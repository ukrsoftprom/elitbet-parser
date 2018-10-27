package com.elitbet.parser.impl.saver;

import com.elitbet.parser.model.DataObject;

import java.util.List;

public interface Saver {
    void save(DataObject dataObject);

    void save(DataObject... dataObjects);

    void save(List<DataObject> dataObjectList);
}
