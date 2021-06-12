package io.github.murraya.log4j.influxdb;

import org.apache.logging.log4j.core.appender.nosql.NoSqlObject;

import java.util.*;


public class InfluxDbObject implements NoSqlObject<Map<String, Object>> {

    private final Map<String, Object> map = new LinkedHashMap<>();

    public void set(String field, Object value) {
        this.map.put(field, value);
    }

    public void set(String field, NoSqlObject<Map<String, Object>> value) {
        this.map.put(field, value.unwrap());
    }

    public void set(String field, Object[] values) {
        this.map.put(field, Arrays.asList(values));
    }

    public void set(String field, NoSqlObject<Map<String, Object>>[] values) {
        var list = new ArrayList(values.length);
        NoSqlObject[] arr$ = values;
        int len$ = values.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            NoSqlObject<Map<String, Object>> value = arr$[i$];
            list.add(value.unwrap());
        }

        this.map.put(field, list);
    }

    public Map<String, Object> unwrap() {
        return this.map;
    }


    @Override
    public String toString() {
        return "InfluxDbObject{" +
                "map=" + map +
                '}';
    }
}
