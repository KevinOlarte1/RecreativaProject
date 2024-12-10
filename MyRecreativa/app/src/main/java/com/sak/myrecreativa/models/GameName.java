package com.sak.myrecreativa.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GameName implements Parcelable {
    private final String name;
    private int maxScore;

    private boolean favorite;

    private List<Mission> missions;

    public GameName(String name){
        this.name = name;
        this.maxScore = 0;
        this.favorite = false;
        this.missions = new ArrayList<>();
    }

    public void updateMission(){
        for (Mission m: missions) {
            m.setCurrentPoints((int) maxScore);
        }
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }

    public List<Mission> getMissions() {
        return missions;
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

