package com.alwaystinkering.parser;

public class SessionData {
    private String name;
    private int startIndex;
    private int endIndex;

    public SessionData() {
        name = "";
        startIndex = -1;
        endIndex = -1;
    }

    public SessionData(SessionData rhs) {
        this.name = rhs.name;
        this.startIndex = rhs.startIndex;
        this.endIndex = rhs.endIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public String toString() {
        return name + ": start[" + startIndex + "] end[" + endIndex + "]";
    }
}
