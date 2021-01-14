package com.example.mytestdiary;

import android.os.Parcel;
import android.os.Parcelable;

public class DBDateCode implements Parcelable {

    private int numDiaryYear;
    private int numDiaryMonth;
    private int numDiaryDay;
    private String strDayName;


    public int getNumDiaryYear() {
        return numDiaryYear;
    }
    public void setNumDiaryYear(int numDiaryYear) {
        this.numDiaryYear = numDiaryYear;
    }

    public int getNumDiaryMonth() {
        return numDiaryMonth;
    }
    public void setNumDiaryMonth(int numDiaryMonth) {
        this.numDiaryMonth = numDiaryMonth;
    }

    public int getNumDiaryDay() {
        return numDiaryDay;
    }
    public void setNumDiaryDay(int numDiaryDay) {
        this.numDiaryDay = numDiaryDay;
    }

    public String getStrDayName() {
        return strDayName;
    }
    public void setStrDayName(String strDayName) {
        this.strDayName = strDayName;
    }

    public void setStrDayName(int numDay) {

    }

    protected DBDateCode(Parcel in) {

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(numDiaryYear);
        parcel.writeInt(numDiaryMonth);
        parcel.writeInt(numDiaryDay);
        parcel.writeString(strDayName);
    }

    public static final Creator<DBDateCode> CREATOR = new Creator<DBDateCode>() {
        @Override
        public DBDateCode createFromParcel(Parcel in) {
            return new DBDateCode(in);
        }

        @Override
        public DBDateCode[] newArray(int size) {
            return new DBDateCode[size];
        }
    };
}
