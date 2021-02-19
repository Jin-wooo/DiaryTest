package com.example.mytestdiary;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebStorage;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mytestdiary.DiaryList.DiaryInfo;
import com.example.mytestdiary.DiaryList.DiaryListAdapter;
import com.example.mytestdiary.DiaryList.DiaryListDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final static int NO_DATE = 0;
    private final static int NO_IDX = -1;
    private final static int DIARY_ITEM = 0;
    private final static int DAY_SEP_LINE = 1;
    private final static String LOG_TAG = "MainActivity";

    DiaryWriteFragment diaryWriteFragment;
    FragmentTransaction diaryTransaction;

    private DiaryListAdapter diaryListAdapter;

    // Buttons
    private FloatingActionButton fabNewDiary;
    private Button btnYear;
    private Button btnMonth;
    private ImageButton mImgBtnDelete;

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
            if (mImgBtnDelete.getVisibility() == View.VISIBLE) {
                // 현재 삭제 모드. 삭제 모드를 해제하는 메서드
                setDeleteMode(false);
            }
            else {

                // 현재 기본 화면임. 뒤로 가기로 종료하기 실행
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

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate");

        init();
        setDiaryList();
        // 다이어리 아이템 클릭했을 때 작동하는 함수.
        // 당연하겠지만 내가 적은 일기장을 띄워주겠죠? 이건 DB랑 연동해서 작업해봅시다.
        diaryListAdapter.setOnItemClickListener(new DiaryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                // 삭제할 때는 클릭이 되면 안되자너? 그러니까 원래 클릭 기능을 막아야지.
                if (mImgBtnDelete.getVisibility() == View.VISIBLE) {
                     return;
                }
                // 리스트 기록 상의 날짜를 보내줘야 함
                DiaryInfo tempInfo = diaryListAdapter.getItem(pos);
                Bundle bundle = new Bundle();
                bundle.putParcelable("date", tempInfo.getDbDateCode());
                bundle.putInt("idx", tempInfo.getNumIdxCode());
                bundle.putBoolean("isWritten", true);

                openDiary(bundle);
            }
        });

        diaryListAdapter.setOnItemLongClickListener(new DiaryListAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int pos) {
                Log.d(LOG_TAG, "INSIDE Mains");
                if (mImgBtnDelete.getVisibility() != View.VISIBLE) {
                    setDeleteMode(true);
                }
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
                Cursor diaryCursor = db.query("diarylist", getResources().getStringArray(R.array.all_column_names), "date=?",
                        new String[]{todayDateCode.getStrDateCode()},
                        null, null, "idx DESC");
                diaryCursor.moveToFirst();
//                showResult(diaryCursor);

                int newIdx = 0; // 기본적으로는 0이 세팅됨.
                try {
                    if (diaryCursor != null && diaryCursor.getCount() > 0) {
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
                bundle.putInt("idx", newIdx); // 이 버튼을 눌렀으면 그 날의 가장 최신 일기.
                bundle.putBoolean("isWritten", false);
                openDiary(bundle);
            }
        });

        mImgBtnDelete = (ImageButton) findViewById(R.id.btnDelete);
        mImgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int prevLineIdx = 0;
                ArrayList<DiaryInfo> listOriginInfo = diaryListAdapter.getDiaryList();
                DiaryInfo prevInfo, deleteInfo;
                SQLiteDatabase delDB = MainDBHelper.getReadableDatabase();
                Cursor chkCursor = delDB.rawQuery("SELECT * FROM diarylist", null);
                showResult(chkCursor);

                for (int iterDel = 0; iterDel < listOriginInfo.size(); iterDel++) {
                    deleteInfo = listOriginInfo.get(iterDel);
                    // 체크한 항목을 찾아냅니다.(값으로 긁어올 수 있게 작업함)
                    if (deleteInfo.isChecked()) {
                        // 어댑터 내에서 아이템을 제거합니다. (시각적으로 제거)
                        diaryListAdapter.removeItem(iterDel);

                        // 만약 그게 그 날짜의 마지막 일기라면, 날짜구분선도 날려야겠죠.
                        prevLineIdx = iterDel - 1;
                        prevInfo = listOriginInfo.get(prevLineIdx);
                        // 날짜구분선인 경우에만 작동
                        if (prevInfo.getNumIdxCode() == -1) {
                            if (prevLineIdx == 0) {
                                // 내 밑의 아이템이 나보다 날짜가 더 클 때(다를 때...도 가능할듯)
                                if (prevInfo.getNumDateCode() < listOriginInfo.get(prevLineIdx + 1).getNumDateCode()) {
                                    diaryListAdapter.removeItem(prevLineIdx);
                                }
                            }
                            else if (prevLineIdx == listOriginInfo.size() - 1) {
                                // 내 위의 아이템이 나보다 날짜가 더 작을 때(내 위로 다른 날짜의 일기)
                                if (prevInfo.getNumDateCode() > listOriginInfo.get(prevLineIdx - 1).getNumDateCode()) {
                                    diaryListAdapter.removeItem(prevLineIdx);
                                }
                            }
                            else {
                                // 내 위아래가 다 나랑 날짜가 다름(필요없는 구분선)
                                if (prevInfo.getNumDateCode() < listOriginInfo.get(prevLineIdx + 1).getNumDateCode() ||
                                        prevInfo.getNumDateCode() > listOriginInfo.get(prevLineIdx - 1).getNumDateCode()) {
                                    diaryListAdapter.removeItem(prevLineIdx);
                                }
                            }
                        }

                        // DB에서도 제거합니다. (DB 내 제거)
                        delDB = MainDBHelper.getWritableDatabase();
                        delDB.delete("diarylist", "date=? AND idx=?", new String[]{deleteInfo.getStrDateCode(), deleteInfo.getStrIdxCode()});
                    }
                    chkCursor = delDB.rawQuery("SELECT * FROM diarylist", null);
                    showResult(chkCursor);
                }
                // 변경 시항을 저장합니다.
                diaryListAdapter.notifyDataSetChanged();
                MainDBHelper.close();
                chkCursor.close();
                setDeleteMode(false);
            }
        });
    }

    private void init() {
        // 클래스 변수 세팅
        RecyclerView rvDiaryList = findViewById(R.id.rvDiaryList);

        LinearLayoutManager diaryListManager = new LinearLayoutManager(this);
        rvDiaryList.setLayoutManager(diaryListManager);
        DiaryListDecoration diaryListDecoration = new DiaryListDecoration(15);
        rvDiaryList.addItemDecoration(diaryListDecoration);


        diaryListAdapter = new DiaryListAdapter();
        rvDiaryList.setAdapter(diaryListAdapter);

        MainDBHelper = new DiaryDBHelper(this);
        todayDateCode = new DBDateCode();

        // MainActivity 기본 세팅
        updateTime();

        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);


//        Button button21 = (Button) findViewById(R.id.button21);
//        button21.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar cal = Calendar.getInstance();
//                Log.d(LOG_TAG, Integer.toString(cal.get(Calendar.DAY_OF_WEEK)));
//                cal.set(2021, Calendar.JANUARY, 28);
//                Log.d(LOG_TAG, Integer.toString(cal.get(Calendar.DAY_OF_WEEK)));
//                cal.set(2021, Calendar.MARCH, 4);
//                Log.d(LOG_TAG, Integer.toString(cal.get(Calendar.DAY_OF_WEEK)));
//                Log.d(LOG_TAG, Integer.toString(cal.get(Calendar.DAY_OF_WEEK)));
//            }
//        });
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
        todayDateCode.setStrDiaryYear(new SimpleDateFormat("yyyy", Locale.KOREA).format(date));
        todayDateCode.setStrDiaryMonth(new SimpleDateFormat("MM", Locale.KOREA).format(date));
        todayDateCode.setStrDiaryDay(new SimpleDateFormat("dd", Locale.KOREA).format(date));
        todayDateCode.setStrDayName(new SimpleDateFormat("EEE", Locale.ENGLISH).format(date));
    }

    public void openDiary(Bundle bundle) {
        diaryWriteFragment = new DiaryWriteFragment();
        diaryWriteFragment.setArguments(bundle);
        fabNewDiary.hide();
        diaryTransaction = getSupportFragmentManager().beginTransaction();
        diaryTransaction.add(R.id.containerLayout, diaryWriteFragment);
        diaryTransaction.addToBackStack(null);
        diaryTransaction.commit();
    }

    public void closeDiary() {
        fabNewDiary.show();
        diaryTransaction = getSupportFragmentManager().beginTransaction();
        diaryTransaction.remove(diaryWriteFragment);
        diaryTransaction.commit();
    }

    public void setDeleteMode(boolean isDeleteMode) {
        if (isDeleteMode) {
            mImgBtnDelete.setVisibility(View.VISIBLE);
            fabNewDiary.hide();
            diaryListAdapter.setCheckBoxVisibility(true);
        }
        else {
            mImgBtnDelete.setVisibility(View.GONE);
            fabNewDiary.show();
            diaryListAdapter.setCheckBoxVisibility(false);
            diaryListAdapter.setAllCheckBoxValue(false);
        }
        diaryListAdapter.notifyDataSetChanged();
    }

    private void setDiaryList() {
        SQLiteDatabase database = MainDBHelper.getReadableDatabase();
        Cursor setCursor = database.query("diarylist",
                getResources().getStringArray(R.array.all_column_names), null,
                null, null, null, "date ASC, idx ASC");

        String setDate, setTitle, setContent, setDayName;
        int setIdx;
        DiaryInfo setInfo;
        int prevDate = 0;

        while (setCursor.moveToNext()) {
            setInfo = new DiaryInfo();
            setDate = setCursor.getString(setCursor.getColumnIndex("date"));
            setIdx = setCursor.getInt(setCursor.getColumnIndex("idx"));
            setTitle = setCursor.getString(setCursor.getColumnIndex("title"));
//                    .substring(0, getResources().getInteger(R.integer.title_max_length));
            setContent = setCursor.getString(setCursor.getColumnIndex("content"));
//                    .substring(0, getResources().getInteger(R.integer.content_max_length));
            setDayName = setCursor.getString(setCursor.getColumnIndex("dayname"));

            // 그 날의 맨 처음 일기라면, 날짜분리선을 달아줘야 한다.
            if (Integer.parseInt(setDate) > prevDate) {
                DiaryInfo sepInfo = new DiaryInfo();
                sepInfo.setStrDateCode(setDate, setDayName);
                sepInfo.setNumTypeCode(DAY_SEP_LINE);
                sepInfo.setNumIdxCode(-1); // 날짜분리선이므로 인덱스의 적용을 받지 않는 아이템임

                diaryListAdapter.setItem(sepInfo);
            }

            Log.d(LOG_TAG, "< setDiary setting DB > ");
            Log.d(LOG_TAG, "date : " + setDate);
            Log.d(LOG_TAG, "idx : " + setIdx);
            Log.d(LOG_TAG, "Title : " + setTitle);
            Log.d(LOG_TAG, "Content : " + setContent);
            Log.d(LOG_TAG, "Dayname : " + setDayName);

            setInfo.setStrDateCode(setDate, setDayName);
            setInfo.setNumIdxCode(setIdx);
            setInfo.setStrDiaryTitle(setTitle);
            setInfo.setStrDiaryContent(setContent);
            setInfo.setNumTypeCode(DIARY_ITEM);

            diaryListAdapter.setItem(setInfo);
            prevDate = Integer.parseInt(setDate);
        }
        diaryListAdapter.notifyDataSetChanged();
        setCursor.close();
        MainDBHelper.close();
    }

    /*
     리스트의 아이템 하나를 세팅하는 함수입니다.
    해당 날짜의 가장 위일 경우, 날짜구분을 위한 아이템이 하나 더 들어갑니다.
            */
    public void setListItem(DiaryInfo info) {
        ArrayList<DiaryInfo> settingList = diaryListAdapter.getDiaryList();
        int setDate = info.getNumDateCode();
        int iterSet = 0;

        // 이 아이템이 어느 위치로 들어가야할지 정합니다.
        while (setDate >= settingList.get(iterSet).getNumDateCode()) {
            iterSet++;
        }
        diaryListAdapter.setItem(iterSet, info);

        if (iterSet == 0 || setDate > settingList.get(iterSet - 1).getNumDateCode()) {
            DiaryInfo sepInfo = new DiaryInfo();
            sepInfo.setStrDateCode(info.getStrDateCode(), info.getDbDateCode().getStrDayName());
            sepInfo.setNumTypeCode(DAY_SEP_LINE);

            diaryListAdapter.setItem(iterSet, sepInfo);
        }
        diaryListAdapter.notifyDataSetChanged();
    }

    public void updateListItem(String strCode, int dbIdx, DiaryInfo info) {
        ArrayList<DiaryInfo> updateList = diaryListAdapter.getDiaryList();
        DiaryInfo tmpInfo;
        int listIdx = 0;

        for (int iterFind = 0; iterFind < updateList.size(); iterFind++) {
            tmpInfo = updateList.get(iterFind);
            if (tmpInfo.getStrDateCode().equals(strCode) && tmpInfo.getNumIdxCode() == dbIdx) {
                listIdx = iterFind;
                break;
            }
        }

        try {
            diaryListAdapter.updateItem(listIdx, info);
            diaryListAdapter.notifyDataSetChanged();
        }
        catch (Exception e) {
            Log.d(LOG_TAG, "updateListItem Error. Maybe List IDX");
        }
    }

    protected void showResult(Cursor cur) {
        int date = cur.getColumnIndex("date");
        int idx = cur.getColumnIndex("idx");
        int title_col = cur.getColumnIndex("title");
        int ctt_col = cur.getColumnIndex("content");
        int namecol = cur.getColumnIndex("dayname");
        while (cur.moveToNext()) {
            Log.d(LOG_TAG, "< Now In DB > ");
            Log.d(LOG_TAG, "date : " + cur.getString(date));
            Log.d(LOG_TAG, "idx : " + cur.getString(idx));
            Log.d(LOG_TAG, "Title : " + cur.getString(title_col));
            Log.d(LOG_TAG, "Content : " + cur.getString(ctt_col));
            Log.d(LOG_TAG, "Dayname : " + cur.getString(namecol));
        }
        cur.moveToFirst();
    }
}