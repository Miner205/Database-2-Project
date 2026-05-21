package com.project.artconnect.model;

import java.time.LocalTime;

public class OpeningHours {
    private String day;
    private LocalTime openingTime;
    private LocalTime closingTime;

    public OpeningHours() {
    }

    public OpeningHours(String day, LocalTime openingTime, LocalTime closingTime) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {
        return day + " : " + openingTime + " - " + closingTime;
    }

}
