package com.alwaystinkering.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogData {

    private String sessionName;
    private Date start;
    private Date end;
    private List<GeoPoint> path = new ArrayList<>();

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<GeoPoint> getPath() {
        return path;
    }

    public void setPath(List<GeoPoint> path) {
        this.path = path;
    }
}
