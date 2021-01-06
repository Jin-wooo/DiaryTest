package com.example.mytestdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DiaryListAdapter diaryListAdapter;

    // Buttons
    private FloatingActionButton fabNewDiary;



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

            }
        });
    }

    private void init() {
        RecyclerView rvDiaryList = findViewById(R.id.rvDiaryList);

        LinearLayoutManager diaryListManager = new LinearLayoutManager(this);
        rvDiaryList.setLayoutManager(diaryListManager);

        diaryListAdapter = new DiaryListAdapter();
        rvDiaryList.setAdapter(diaryListAdapter);
    }
}