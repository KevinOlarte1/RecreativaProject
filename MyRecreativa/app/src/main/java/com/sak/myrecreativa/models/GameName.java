package com.sak.myrecreativa.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GameName implements Parcelable {
    private final String name;
    private long maxScore;

    private boolean blocked;

    public GameName(String name){
        this.name = name;
        this.maxScore = 0;
        this.blocked = true;
    }

    public void setMaxScore(long maxScore) {
        this.maxScore = maxScore;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public long getMaxScore() {
        return maxScore;
    }

    public String getName() {
        return name;
    }

    public boolean isBlocked() {
        return blocked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
