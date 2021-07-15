package com.openclassroom.go4lunch.model;

import java.util.List;

public class User {

    private int mUid;
    private String mDisplayName;
    private List<String> mLikeList;
    private String mAvatarUrl;

    public User(int id, String displayName, List<String> likeList, String avatarUrl) {
        mUid = id;
        mDisplayName = displayName;
        mLikeList = likeList;
        mAvatarUrl = avatarUrl;
    }

    public int getUid() {
        return mUid;
    }

    public void setUid(int uid) {
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
}
