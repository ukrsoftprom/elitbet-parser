package com.elitbet.parser.impl.saver;

import com.elitbet.parser.model.DataObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class HttpSaver implements Saver {
    private final static Logger logger = Logger.getLogger(HttpSaver.class.getName());
    private HttpURLConnection httpURLConnection;
    private String host;

    public HttpSaver() {
    }
    public HttpSaver(URL url) {
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    protected void finalize() {
        httpURLConnection.disconnect();
    }

    @Override
    public void save(DataObject dataObject) {
        URL url = null;
        try {
            System.out.println(host + dataObject.toURL());
            url = new URL(host + dataObject.toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null) {
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                logger.info("Saving: " + url.toExternalForm());
                int responseCode = httpURLConnection.getResponseCode();
                logger.info("Response code: " + Integer.valueOf(responseCode));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(DataObject... dataObjects) {
        for (DataObject dataObject : dataObjects) {
            save(dataObject);
        }
    }

    @Override
    public void save(List<DataObject> dataObjectList) {
        dataObjectList.forEach(this::save);
    }


}
