package com.example.mytestdiary;

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

    public DBDateCode() {
        this.strDiaryYear = "";
        this.strDiaryMonth = "";
        this.strDiaryDay = "";
        this.strDayName = "";
    }

    protected int getDateCode() {
        return Integer.parseInt(strDiaryYear + strDiaryMonth + strDiaryDay);
    }

    protected String getStrDateCode() {
        return strDiaryYear + strDiaryMonth + strDiaryDay;
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
