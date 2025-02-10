package com.example.global;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GlobalMap {

    private static GlobalMap instance;

    private final Map<GlobalMapKey, Object> data = new HashMap<>();

    public static GlobalMap getInstance() {
        synchronized (GlobalMap.class) {
            if (instance == null) {
                instance = new GlobalMap();
            }
        }
        return instance;
    }

    public void put(GlobalMapKey key, Object value) {
        log.debug("[GLOBALMAP] Set data:" + value);
        data.put(key, value);
    }

    public Object get(GlobalMapKey key) {
        var obj = data.get(key);
        log.debug("[GLOBALMAP] Get data:" + obj);
        return obj;
    }



    public Object getOrDefault(GlobalMapKey key, Object defaultObj) {
        var obj = data.getOrDefault(key, defaultObj);
        log.debug("[GLOBALMAP] Get data:" + obj);
        return obj;
    }

}
