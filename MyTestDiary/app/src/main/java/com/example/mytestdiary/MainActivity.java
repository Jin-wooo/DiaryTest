package com.example.mytestdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.FrameMetrics;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final static int NO_DATE = 0;
    private final static int NO_IDX = -1;

    DiaryWriteFragment diaryWriteFragment;
    FragmentTransaction diaryTransaction;

    private DiaryListAdapter diaryListAdapter;

    // Buttons
    private FloatingActionButton fabNewDiary;

    public DiaryDBHelper getDiaryDBHelper() {
        return diaryDBHelper;
    }
    protected DiaryDBHelper diaryDBHelper;

    Calendar calendar;
    String[] colHeads = {"date", "idx"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        calendar = Calendar.getInstance();
        // 다이어리 아이템 클릭했을 때 작동하는 함수.
        // 당연하겠지만 내가 적은 일기장을 띄워주겠죠? 이건 DB랑 연동해서 작업해봅시다.
        diaryListAdapter.setOnItemClickListener(new DiaryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                DiaryInfo tempInfo = diaryListAdapter.getItem(pos);

                Bundle bundle = new Bundle();
                bundle.putInt("date", tempInfo.getDBDateCode());
                bundle.putInt("idx", tempInfo.getNumIdxCode());
                diaryWriteFragment.setArguments(bundle);
            }
        });
        fabNewDiary = (FloatingActionButton) findViewById(R.id.fabNewDiary);
        fabNewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fabNewDiary.hide();

                diaryTransaction = getSupportFragmentManager().beginTransaction();
                diaryTransaction.add(R.id.containerLayout, diaryWriteFragment);
                diaryTransaction.commit();
            }
        });
    }

    private void init() {
        RecyclerView rvDiaryList = findViewById(R.id.rvDiaryList);

        LinearLayoutManager diaryListManager = new LinearLayoutManager(this);
        rvDiaryList.setLayoutManager(diaryListManager);

        diaryListAdapter = new DiaryListAdapter();
        rvDiaryList.setAdapter(diaryListAdapter);

        diaryWriteFragment = new DiaryWriteFragment();

        diaryDBHelper = new DiaryDBHelper(this);
    }

    public void closeDiary() {
        fabNewDiary.show();
        diaryTransaction = getSupportFragmentManager().beginTransaction();
        diaryTransaction.remove(diaryWriteFragment);
        diaryTransaction.commit();
    }
}