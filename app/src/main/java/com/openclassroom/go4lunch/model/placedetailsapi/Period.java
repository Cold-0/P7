package com.openclassroom.go4lunch.model.placedetailsapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Period implements Serializable {

    @SerializedName("close")
    private Close mClose;
    @SerializedName("open")
    private Open mOpen;

    public Close getClose() {
        return mClose;
    }

    public void setClose(Close close) {
        mClose = close;
    }

    public Open getOpen() {
        return mOpen;
    }

    public void setOpen(Open open) {
        mOpen = open;
    }
}
