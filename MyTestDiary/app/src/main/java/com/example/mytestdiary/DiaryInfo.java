package com.example.mytestdiary;

import androidx.annotation.NonNull;

public class DiaryInfo {
    private String strDiaryTitle;
    private String strDiaryContent;
    private DBDateCode dbDateCode;
    private int numTypeCode;
    private int numIdxCode;



    public String getStrDiaryTitle() {
        return strDiaryTitle;
    }
    public void setStrDiaryTitle(String strDiaryTitle) {
        if (strDiaryTitle.equals(""))
            this.strDiaryTitle = "No Title";
        else
            this.strDiaryTitle = strDiaryTitle;
    }

    public String getStrDiaryContent() {
        return strDiaryContent;
    }
    public void setStrDiaryContent(String strDiaryContent) {
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


    @NonNull
    @Override
    public String toString() {
        return super.toString();
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
}