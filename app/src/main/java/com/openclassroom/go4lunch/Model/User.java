package com.openclassroom.go4lunch.Model;

import java.util.List;

public class User {

    private int mId;
    private String mDisplayName;
    private List<String> mLikeList;
    private String mAvatarUrl;

    public User(int id, String displayName, List<String> likeList, String avatarUrl) {
        mId = id;
        mDisplayName = displayName;
        mLikeList = likeList;
        mAvatarUrl = avatarUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
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
