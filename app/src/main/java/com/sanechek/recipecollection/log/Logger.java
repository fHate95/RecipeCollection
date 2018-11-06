package com.sanechek.recipecollection.log;

import java.util.LinkedList;
import java.util.List;

public class Logger {

    private static final int MAX_LOG_SIZE = 1000;

    private static LinkedList<LoggerData> loggerData = new LinkedList<>();

    public void addLogData(LoggerData data) {
        loggerData.addLast(data);
        int size = loggerData.size();
        if(size > MAX_LOG_SIZE){
            int diff = size - MAX_LOG_SIZE;
            for(int i = 0; i < diff; i++){
                loggerData.removeFirst();
            }
        }
    }

    public static List<LoggerData> getLogs() {
        return loggerData;
    }
}
