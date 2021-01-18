package com.example.mytestdiary;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DiaryInfo implements Parcelable {
    private String strDiaryTitle;
    private String strDiaryContent;
    private DBDateCode dbDateCode;
    private int numTypeCode;
    private int numIdxCode;


    protected DiaryInfo(Parcel in) {
        strDiaryTitle = in.readString();
        strDiaryContent = in.readString();
        dbDateCode = in.readParcelable(DBDateCode.class.getClassLoader());
        numTypeCode = in.readInt();
        numIdxCode = in.readInt();
    }

    public static final Creator<DiaryInfo> CREATOR = new Creator<DiaryInfo>() {
        @Override
        public DiaryInfo createFromParcel(Parcel in) {
            return new DiaryInfo(in);
        }

        @Override
        public DiaryInfo[] newArray(int size) {
            return new DiaryInfo[size];
        }
    };

    public String getStrDiaryTitle() {
        return strDiaryTitle;
    }
    public void setStrDiaryTitle(String strDiaryTitle) {
        if (strDiaryTitle.isEmpty())
            this.strDiaryTitle = "No Title";
        else if (strDiaryTitle.equals("No Title"))
            return;
        else
            this.strDiaryTitle = strDiaryTitle;
    }

    public String getStrDiaryContent() {
        return strDiaryContent;
    }
    public void setStrDiaryContent(String strDiaryContent) {
        if (strDiaryContent.isEmpty())
            this.strDiaryContent = "No Content";
        else if (strDiaryContent.equals("No Content"))
            return;
        else
            this.strDiaryContent = strDiaryContent;
    }

    public DBDateCode getDbDateCode() {
        return dbDateCode;
    }
    public void setDbDateCode(DBDateCode dbDateCode) {
        this.dbDateCode = dbDateCode;
    }

    public int getNumTypeCode() {
        return numTypeCode;
    }
    public void setNumTypeCode(int numTypeCode) {
        this.numTypeCode = numTypeCode;
    }

    public int getNumIdxCode() {
        return numIdxCode;
    }
    public void setNumIdxCode(int numIdxCode) {
        this.numIdxCode = numIdxCode;
    }

    public int getDateCode() {
        return dbDateCode.getDateCode();
    }

    public String getStrDateCode() {
        return dbDateCode.getStrDateCode();
    }


    public DiaryInfo() {
        this.strDiaryTitle = "";
        this.strDiaryContent = "";
        this.dbDateCode = new DBDateCode();
        this.numTypeCode = 0;
        this.numIdxCode = 0;
    }

    public DiaryInfo(DiaryInfo diaryInfo) {
        this.strDiaryTitle = diaryInfo.strDiaryTitle;
        this.strDiaryContent = diaryInfo.strDiaryContent;
        this.dbDateCode = diaryInfo.dbDateCode;
        this.numTypeCode = diaryInfo.numTypeCode;
        this.numIdxCode = diaryInfo.numIdxCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(strDiaryTitle);
        parcel.writeString(strDiaryContent);
        parcel.writeParcelable(dbDateCode, i);
        parcel.writeInt(numTypeCode);
        parcel.writeInt(numIdxCode);
    }
}