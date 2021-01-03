package com.example.mytestdiary;

public class DiaryInfo {
    private String strDiaryTitle;
    private String strDiaryContent;
    private int numDiaryWriteMonth;
    private int numDiaryWriteDay;

    public String getStrDiaryTitle() {
        return strDiaryTitle;
    }

    public void setStrDiaryTitle(String strDiaryTitle) {
        this.strDiaryTitle = strDiaryTitle;
    }

    public String getStrDiaryContent() {
        return strDiaryContent;
    }

    public void setStrDiaryContent(String strDiaryContent) {
        this.strDiaryContent = strDiaryContent;
    }

    public int getNumDiaryWriteMonth() {
        return numDiaryWriteMonth;
    }

    public void setNumDiaryWriteMonth(int numDiaryWriteMonth) {
        this.numDiaryWriteMonth = numDiaryWriteMonth;
    }

    public int getNumDiaryWriteDay() {
        return numDiaryWriteDay;
    }

    public void setNumDiaryWriteDay(int numDiaryWriteDay) {
        this.numDiaryWriteDay = numDiaryWriteDay;
    }

    public DiaryInfo() {
        this.strDiaryTitle = "";
        this.strDiaryContent = "";
        this.numDiaryWriteMonth = 0;
        numDiaryWriteDay = 0;
    }

    public DiaryInfo(String strDiaryTitle, String strDiaryContent, int numDiaryWriteMonth, int numDiaryWriteDay) {
        this.strDiaryTitle = strDiaryTitle;
        this.strDiaryContent = strDiaryContent;
        this.numDiaryWriteMonth = numDiaryWriteMonth;
        this.numDiaryWriteDay = numDiaryWriteDay;
    }
}
