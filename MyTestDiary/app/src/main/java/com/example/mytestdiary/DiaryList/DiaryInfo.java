package com.example.mytestdiary.DiaryList;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mytestdiary.DBDateCode;

public class DiaryInfo implements Parcelable {
    private String strDiaryTitle;
    private String strDiaryContent;
    private DBDateCode dbDateCode;
    private int numTypeCode;
    private int numIdxCode;
    private boolean isChecked;
    // Bool은 특별한 방식으로 체크합니다.
    // 특히 writeBoolean이 API 29가 최소이므로 그냥 예전 방식 차용.
 

    protected DiaryInfo(Parcel in) {
        strDiaryTitle = in.readString();
        strDiaryContent = in.readString();
        dbDateCode = in.readParcelable(DBDateCode.class.getClassLoader());
        numTypeCode = in.readInt();
        numIdxCode = in.readInt();
        isChecked = in.readInt() != 0;
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
    public String getStrIdxCode() { return Integer.toString(numIdxCode); }
    public void setNumIdxCode(int numIdxCode) {
        this.numIdxCode = numIdxCode;
    }

    public int getNumDateCode() {
        return dbDateCode.getNumDateCode();
    }
    public String getStrDateCode() {
        return dbDateCode.getStrDateCode();
    }
    public void setStrDateCode(String code) { this.dbDateCode.setStrDateCode(code); }
    public void setStrDateCode(String code, String dayName) {
        this.dbDateCode.setStrDateCode(code, dayName);
    }

    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public DiaryInfo() {
        this.strDiaryTitle = "";
        this.strDiaryContent = "";
        this.dbDateCode = new DBDateCode();
        this.numTypeCode = 0;
        this.numIdxCode = 0;
        this.isChecked = false;
    }

    public DiaryInfo(DiaryInfo diaryInfo) {
        this.strDiaryTitle = diaryInfo.strDiaryTitle;
        this.strDiaryContent = diaryInfo.strDiaryContent;
        this.dbDateCode = diaryInfo.dbDateCode;
        this.numTypeCode = diaryInfo.numTypeCode;
        this.numIdxCode = diaryInfo.numIdxCode;
        this.isChecked = diaryInfo.isChecked;
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
        parcel.writeInt(isChecked ? 1 : 0); // True = 1, False = 0
    }
}