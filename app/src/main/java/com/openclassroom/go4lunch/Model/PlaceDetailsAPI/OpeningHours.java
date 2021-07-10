package com.openclassroom.go4lunch.Model.PlaceDetailsAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.util.List;

public class OpeningHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("open_now")
    public Boolean openNow;

    public Boolean getOpenNow() {
        return openNow;
    }

    @SerializedName("periods")
    private List<Period> mPeriods;

    public List<Period> getPeriods() {
        return mPeriods;
    }

    public void setPeriods(List<Period> periods) {
        mPeriods = periods;
    }

    @SerializedName("weekday_text")
    public String[] weekdayText;

    public String[] getWeekdayText() {
        return weekdayText;
    }

    public Boolean permanentlyClosed;

    public Boolean getPermanentlyClosed() {
        return permanentlyClosed;
    }


}
