package com.example.mytestdiary;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

public class DBDateCode implements Parcelable {
    private final static String LOG_TAG = "DBDateCode";
    private String strDiaryYear;
    private String strDiaryMonth;
    private String strDiaryDay;
    private String strDayName;

    // Year`s Getter and Setter
    public String getStrDiaryYear() {
        return strDiaryYear;
    }
    public int getStrDiaryYearToInt() {
        return Integer.parseInt(strDiaryYear);
    }
    public void setStrDiaryYear(String strDiaryYear) {
        this.strDiaryYear = strDiaryYear;
    }
    public void setStrDiaryYear(int diaryYear) {
        this.strDiaryYear = Integer.toString(diaryYear);
    }

    // Month`s Getter and Setter
    public String getStrDiaryMonth() {
        return strDiaryMonth;
    }
    public int getStrDiaryMonthToInt() {
        return Integer.parseInt(strDiaryMonth);
    }
    public void setStrDiaryMonth(String strDiaryMonth) {
//        if (strDiaryMonth.length() == 1) {
//            this.strDiaryMonth = "0" + strDiaryMonth;
//        }
//        else {
//            this.strDiaryMonth = strDiaryMonth;
//        }
        this.strDiaryMonth = strDiaryMonth.length() == 1 ? "0" + strDiaryMonth : strDiaryMonth;
    }
    public void setStrDiaryMonth(int diaryMonth) {
        setStrDiaryMonth(Integer.toString(diaryMonth));
    }

    // Day`s Getter and Setter
    public String getStrDiaryDay() {
        return strDiaryDay;
    }
    public int getStrDiaryDayToInt() {
        return Integer.parseInt(strDiaryDay);
    }
    public void setStrDiaryDay(String strDiaryDay) {
        this.strDiaryDay = strDiaryDay.length() == 1 ? "0" + strDiaryDay : strDiaryDay;
    }
    public void setStrDiaryDay(int diaryDay) {
        setStrDiaryDay(Integer.toString(diaryDay));
    }

    public String getStrDayName() {
        return strDayName;
    }
    public void setStrDayName(String strDayName) {
        this.strDayName = strDayName;
    }
    public void setStrDayName(int day) {
        if (day > 7) {
            this.strDayName = "???";
            return;
        }
        switch (day) {
            case 1:
                this.strDayName = "Sun";
                break;
            case 2:
                this.strDayName = "Mon";
                break;
            case 3:
                this.strDayName = "Tue";
                break;
            case 4:
                this.strDayName = "Wed";
                break;
            case 5:
                this.strDayName = "Thu";
                break;
            case 6:
                this.strDayName = "Fri";
                break;
            case 7:
                this.strDayName = "Sat";
                break;
        }

    }

    protected DBDateCode(Parcel in) {
        this.strDiaryYear = in.readString();
        this.strDiaryMonth = in.readString();
        this.strDiaryDay = in.readString();
        this.strDayName = in.readString();
    }

    @NonNull
    @Override
    public String toString() {
        return strDiaryYear + " / " +
                strDiaryMonth + " / " +
                strDiaryDay + " : " + strDayName;
    }

    public DBDateCode(DBDateCode code) {
        this.strDiaryYear = code.strDiaryYear;
        this.strDiaryMonth = code.strDiaryMonth;
        this.strDiaryDay = code.strDiaryDay;
        this.strDayName = code.strDayName;
    }

    public DBDateCode(String code) {
        if (code.length() != 8)
            new DBDateCode();
        else {
            this.strDiaryYear = code.substring(0, 3);
            this.strDiaryMonth = code.substring(4, 5);
            this.strDiaryDay = code.substring(6, 7);
        }
        this.strDayName = "";
    }

    public DBDateCode(String code, String DayName) {
        if (code.length() != 8)
            new DBDateCode();
        else {
            this.strDiaryYear = code.substring(0, 3);
            this.strDiaryMonth = code.substring(4, 5);
            this.strDiaryDay = code.substring(6, 7);
        }
        this.strDayName = DayName;
    }

    public DBDateCode() {
        this.strDiaryYear = "";
        this.strDiaryMonth = "";
        this.strDiaryDay = "";
        this.strDayName = "";
    }

    public int getNumDateCode() {
        return Integer.parseInt(strDiaryYear + strDiaryMonth + strDiaryDay);
    }

    public String getStrDateCode() {
        return strDiaryYear + strDiaryMonth + strDiaryDay;
    }

    public void setStrDateCode(String code) {
        if (code.length() != 8)
            new DBDateCode();
        else {
            this.strDiaryYear = code.substring(0, 3);
            this.strDiaryMonth = code.substring(4, 5);
            this.strDiaryDay = code.substring(6, 7);
        }
    }

    public void setStrDateCode(String code, String dayName) {
        if (code.length() != 8)
            new DBDateCode();
        else {
            this.strDiaryYear = code.substring(0, 4);
            this.strDiaryMonth = code.substring(4, 6);
            this.strDiaryDay = code.substring(6, 8);
        }
        this.strDayName = dayName;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(strDiaryYear);
        parcel.writeString(strDiaryMonth);
        parcel.writeString(strDiaryDay);
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
