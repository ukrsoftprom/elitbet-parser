package com.elitbet.parser.impl.saver;

import com.elitbet.parser.model.DataObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class UpdateHttpSaver implements Saver {

    private final static Logger logger = Logger.getLogger(CreateHttpSaver.class.getName());
    private HttpURLConnection httpURLConnection;
    private String host;

    public UpdateHttpSaver() {
    }

    @Override
    public void save(DataObject dataObject) {
        URL url = null;
        try {
            System.out.println(host + "/events/update?" + dataObject.toURL());
            url = new URL(host + "/events/update?" + dataObject.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null) {
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                logger.info("Updating: " + url.toExternalForm());
                int responseCode = httpURLConnection.getResponseCode();
                logger.info("Response code: " + Integer.valueOf(responseCode));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(DataObject... dataObjects) {
        for(DataObject dataObject:dataObjects){
            save(dataObject);
        }
    }

    @Override
    public void save(List<DataObject> dataObjectList) {
        dataObjectList.forEach(this::save);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
