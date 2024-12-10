package com.sak.myrecreativa.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GameName implements Parcelable {
    private final String name;
    private long maxScore;

    private boolean favorite;

    public GameName(String name){
        this.name = name;
        this.maxScore = 0;
        this.favorite = false;
    }

    public void setMaxScore(long maxScore) {
        this.maxScore = maxScore;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getMaxScore() {
        return maxScore;
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return favorite;
    }

    // Parcelable implementation
    protected GameName(Parcel in) {
        name = in.readString();
        maxScore = in.readInt();
    }

    public static final Creator<GameName> CREATOR = new Creator<GameName>() {
        @Override
        public GameName createFromParcel(Parcel in) {
            return new GameName(in);
        }

        @Override
        public GameName[] newArray(int size) {
            return new GameName[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(maxScore);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "GameName{" +
                "name='" + name + '\'' +
                ", maxScore=" + maxScore +
                '}';
    }
}

