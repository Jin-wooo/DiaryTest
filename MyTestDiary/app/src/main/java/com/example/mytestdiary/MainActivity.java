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
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final static int NO_DATE = 0;
    private final static int NO_IDX = -1;

    DiaryWriteFragment diaryWriteFragment;
    FragmentTransaction diaryTransaction;

    private DiaryListAdapter diaryListAdapter;

    // Buttons
    private FloatingActionButton fabNewDiary;
    private Button btnYear;
    private Button btnMonth;

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
                // 리스트 기록 상의 날짜를 보내줘야 함
                DiaryInfo tempInfo = diaryListAdapter.getItem(pos);
                Bundle bundle = new Bundle();
                bundle.putInt("date", tempInfo.getDBDateCode());
                bundle.putInt("idx", tempInfo.getNumIdxCode());
                diaryWriteFragment.setArguments(bundle);

                openDiary();
            }
        });
        fabNewDiary = (FloatingActionButton) findViewById(R.id.fabNewDiary);
        fabNewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜를 보내줘야 함
                Bundle bundle = new Bundle();
                int nowDBDateCode =
                        Integer.parseInt(btnYear.getText().toString()) * 10000 +
                        Integer.parseInt(btnMonth.getText().toString()) * 100;
                bundle.putInt("date", nowDBDateCode);
                bundle.putInt("idx", 0); // 이 버튼을 눌렀으면 그 날의 가장 최신 일기.
                openDiary();
            }
        });
    }

    private void init() {
        // 클래스 변수 세팅
        RecyclerView rvDiaryList = findViewById(R.id.rvDiaryList);

        LinearLayoutManager diaryListManager = new LinearLayoutManager(this);
        rvDiaryList.setLayoutManager(diaryListManager);

        diaryListAdapter = new DiaryListAdapter();
        rvDiaryList.setAdapter(diaryListAdapter);

        diaryWriteFragment = new DiaryWriteFragment();
        diaryDBHelper = new DiaryDBHelper(this);

        // MainActivity 기본 세팅

        long presentTime = System.currentTimeMillis();
        Date date = new Date(presentTime);
        String strYear = new SimpleDateFormat("yyyy").format(date);
        String strMonth = new SimpleDateFormat("mm").format(date);

        btnYear = (Button) findViewById(R.id.btnYear);
        btnYear.setText(strYear);
        btnMonth = (Button) findViewById(R.id.btnMonth);
        btnMonth.setText(strMonth);



    }

    public void openDiary() {
        fabNewDiary.hide();
        diaryTransaction = getSupportFragmentManager().beginTransaction();
        diaryTransaction.add(R.id.containerLayout, diaryWriteFragment);
        diaryTransaction.commit();
    }

    public void closeDiary() {
        fabNewDiary.show();
        diaryTransaction = getSupportFragmentManager().beginTransaction();
        diaryTransaction.remove(diaryWriteFragment);
        diaryTransaction.commit();
    }
}