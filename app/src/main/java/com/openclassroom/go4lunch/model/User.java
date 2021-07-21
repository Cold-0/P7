package com.openclassroom.go4lunch.model;

import java.util.List;

public class User {

    private String mUid;
    private String mDisplayName;
    private List<String> mLikeList;
    private String mAvatarUrl;
    private String mEatingAt;

    public User(String id, String displayName, List<String> likeList, String avatarUrl, String eatingAt) {
        mUid = id;
        mDisplayName = displayName;
        mLikeList = likeList;
        mAvatarUrl = avatarUrl;
        mEatingAt = eatingAt;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public List<String> getLikeList() {
        return mLikeList;
    }

    public void setLikeList(List<String> likeList) {
        mLikeList = likeList;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    public String getEatingAt() {
        return mEatingAt;
    }

    public void setEatingAt(String eatingAt) {
        mEatingAt = eatingAt;
    }
}
