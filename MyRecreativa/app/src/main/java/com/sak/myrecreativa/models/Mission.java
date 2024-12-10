package com.sak.myrecreativa.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Mission implements Parcelable {
    private final String title;
    private int currentPoints;

    private final int totalPoints;

    public Mission(String title, int currentPoints, int totalPoints) {
        this.title = title;
        this.currentPoints = currentPoints;
        this.totalPoints = totalPoints;
    }

    public String getTitle() {
        return title;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }


    // Parcelable implementation
    protected Mission(Parcel in) {
        title = in.readString();
        currentPoints = in.readInt();
        totalPoints = in.readInt();
    }

    public static final Creator<Mission> CREATOR = new Creator<Mission>() {
        @Override
        public Mission createFromParcel(Parcel in) {
            return new Mission(in);
        }

        @Override
        public Mission[] newArray(int size) {
            return new Mission[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(currentPoints);
        dest.writeInt(totalPoints);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "title='" + title + '\'' +
                ", currentPoints=" + currentPoints +
                ", totalPoints=" + totalPoints +
                '}';
    }
}
