package com.example.mytestdiary;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.FrameMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final static int NO_DATE = 0;
    private final static int NO_IDX = -1;
    private final static String LOG_TAG = "MainActivity";

    DiaryWriteFragment diaryWriteFragment;
    FragmentTransaction diaryTransaction;

    private DiaryListAdapter diaryListAdapter;

    // Buttons
    private FloatingActionButton fabNewDiary;
    private Button btnYear;
    private Button btnMonth;

    public DiaryDBHelper getMainDBHelper() {
        return MainDBHelper;
    }

    private DiaryDBHelper MainDBHelper;

    private DBDateCode todayDateCode;
    String[] colHeads = {"date", "idx"};
    private long backKeyPressedTime;


    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(MainActivity.this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        // 다이어리 아이템 클릭했을 때 작동하는 함수.
        // 당연하겠지만 내가 적은 일기장을 띄워주겠죠? 이건 DB랑 연동해서 작업해봅시다.
        diaryListAdapter.setOnItemClickListener(new DiaryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                // 리스트 기록 상의 날짜를 보내줘야 함
                DiaryInfo tempInfo = diaryListAdapter.getItem(pos);
                Bundle bundle = new Bundle();
                bundle.putParcelable("date", tempInfo.getDbDateCode());
                bundle.putInt("idx", tempInfo.getNumIdxCode());
                bundle.putBoolean("isWritten", true);
                diaryWriteFragment.setArguments(bundle);

                openDiary();
            }
        });
        fabNewDiary = (FloatingActionButton) findViewById(R.id.fabNewDiary);
        fabNewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 오늘 날짜를 보내줘야 함
                long presentTime = System.currentTimeMillis();
                Date date = new Date(presentTime);
                updateDateCode(date);

                SQLiteDatabase db = MainDBHelper.getReadableDatabase();
                Cursor diaryCursor = db.query("diarylist", colHeads, "date=?",
                        new String[]{todayDateCode.getStrDateCode()},
                        null, null, "idx");

                int newIdx = 0; // 기본적으로는 0이 세팅됨.
                try {
                    if (diaryCursor != null && diaryCursor.getCount() > 0) {
                        int cursorIdx;
                        newIdx = diaryCursor.getInt(diaryCursor.getColumnIndex("idx")) + 1;
                    }
                }
                catch (Exception e){
                    Log.e(LOG_TAG, "fab Click Error");
                }
                finally {
                    if(diaryCursor != null) {
                        // 내용물이 있든 없든 만든 커서는 닫아야겠죠?
                        diaryCursor.close();
                    }
                }
                MainDBHelper.close();
                Bundle bundle = new Bundle();
                bundle.putParcelable("date", todayDateCode);
                bundle.putString("idx", Integer.toString(newIdx)); // 이 버튼을 눌렀으면 그 날의 가장 최신 일기.
                bundle.putBoolean("isWritten", false);
                diaryWriteFragment.setArguments(bundle);
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
        MainDBHelper = new DiaryDBHelper(this);
        todayDateCode = new DBDateCode();

        // MainActivity 기본 세팅
        updateTime();

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    // 앱 내부 시간을 표시하는 부분을 업데이트합니다.
    public void updateTime() {
        long presentTime = System.currentTimeMillis();
        Date date = new Date(presentTime);
        String strYear = new SimpleDateFormat("yyyy", Locale.KOREA).format(date);
        String strMonth = new SimpleDateFormat("MM", Locale.KOREA).format(date);

        btnYear = (Button) findViewById(R.id.btnYear);
        btnYear.setText(strYear);
        btnMonth = (Button) findViewById(R.id.btnMonth);
        btnMonth.setText(strMonth);

        updateDateCode(date);

    }

    // 앱 내부 시간을 가진 DateCode를 업데이트합니다.
    public void updateDateCode(Date date) {
        String a = new SimpleDateFormat("yyyy", Locale.KOREA).format(date);
        todayDateCode.setStrDiaryYear(a);
        todayDateCode.setStrDiaryMonth(new SimpleDateFormat("MM", Locale.KOREA).format(date));
        todayDateCode.setStrDiaryDay(new SimpleDateFormat("DD", Locale.KOREA).format(date));
        todayDateCode.setStrDayName(new SimpleDateFormat("EEE", Locale.ENGLISH).format(date));
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