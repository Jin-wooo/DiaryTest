package com.example.mytestdiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DiaryDBHelper extends SQLiteOpenHelper {
    public DiaryDBHelper(@Nullable Context context) {
        super(context, "DiaryDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE diarylist(date INTEGER, idx TEXT, title TEXT, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS diarylist;");
        onCreate(sqLiteDatabase);
    }
}
