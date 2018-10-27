package com.elitbet.parser.annotations;

import com.elitbet.parser.annotations.Data;
import com.elitbet.parser.model.DataObject;

import java.lang.reflect.Field;
import java.util.List;

public class DataObjectAnnotationAnalyzer<T> {
    public void analyze(Class<T> clazz, List<DataObject> dataObjectList) {
        System.out.println("ANALYZE");
        if (clazz.isAnnotationPresent(Data.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (DataObject dataObject : dataObjectList) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Class fieldType = field.getType();
                    String fieldName = field.getName();
                    try {
                        System.out.println(fieldName + " " + field.get(dataObject));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

}
