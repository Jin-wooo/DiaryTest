package com.example.mytestdiary;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class DBDateCode implements Parcelable {
    private final static String LOG_TAG = "DBDateCode";
    private String strDiaryYear;
    private String strDiaryMonth;
    private String strDiaryDay;
    private String strDayName;

    public String getStrDiaryYear() {
        return strDiaryYear;
    }
    public void setStrDiaryYear(String strDiaryYear) {
        this.strDiaryYear = strDiaryYear;
    }

    public String getStrDiaryMonth() {
        return strDiaryMonth;
    }
    public void setStrDiaryMonth(String strDiaryMonth) {
        if (strDiaryMonth.length() == 1) {
            this.strDiaryMonth = "0" + strDiaryMonth;
        }
        else {
            this.strDiaryMonth = strDiaryMonth;
        }
    }

    public String getStrDiaryDay() {
        return strDiaryDay;
    }
    public void setStrDiaryDay(String strDiaryDay) {
        if (strDiaryDay.length() == 1) {
            this.strDiaryDay = "0" + strDiaryDay;
        }
        else {
            this.strDiaryDay = strDiaryDay;
        }
    }

    public String getStrDayName() {
        return strDayName;
    }
    public void setStrDayName(String strDayName) {
        this.strDayName = strDayName;
    }

    protected DBDateCode(Parcel in) {
        this.strDiaryYear = in.readString();
        this.strDiaryMonth = in.readString();
        this.strDiaryDay = in.readString();
        this.strDayName = in.readString();
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

    protected int getNumDateCode() {
        return Integer.parseInt(strDiaryYear + strDiaryMonth + strDiaryDay);
    }

    protected String getStrDateCode() {
        return strDiaryYear + strDiaryMonth + strDiaryDay;
    }

    protected void setStrDateCode(String code) {
        if (code.length() != 8)
            new DBDateCode();
        else {
            this.strDiaryYear = code.substring(0, 3);
            this.strDiaryMonth = code.substring(4, 5);
            this.strDiaryDay = code.substring(6, 7);
        }
    }

    protected void setStrDateCode(String code, String dayName) {
        if (code.length() != 8)
            new DBDateCode();
        else {
            this.strDiaryYear = code.substring(0, 3);
            this.strDiaryMonth = code.substring(4, 5);
            this.strDiaryDay = code.substring(6, 7);
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
