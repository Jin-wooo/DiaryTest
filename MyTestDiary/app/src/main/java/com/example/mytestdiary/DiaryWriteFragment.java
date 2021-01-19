package com.example.mytestdiary;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import javax.microedition.khronos.egl.EGLDisplay;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryWriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryWriteFragment extends Fragment {

    private final static String LOG_TAG = "DiaryWriteFragment";
    private final static int DIARY_ITEM = 0;
    private final static int DAY_SEP_LINE = 1;

    // Android`s
    private Button mBtnSave;
    private EditText mEditTextContent;
    private EditText mEditTextDiaryTitle;
    private ImageButton mImgbtnReturn;
    private ImageButton mImgbtnOut;
    private TextView mTvDayScreen;

    private DiaryDBHelper mfragDBHelper;
    private DBDateCode mDiaryDateCode;

    private String mDiaryIdx;
    private boolean mIsWritten;
    private boolean mIsTextChanged;
    String[] mFrag_colHeads = {"date", "idx", "title", "content"};

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mEditTextContent.isFocused() || mEditTextDiaryTitle.isFocused()) {
                    // 작성 중에는 mImgBtnOut과 동일한 기능을 수행합니다.
                }
                else {
                    // 아니면? Return과 동일한 기능, 즉 그냥 나가집니다.
                    ((MainActivity) getActivity()).closeDiary();
                }

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this,backPressedCallback);

    }

    public DiaryWriteFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final ViewGroup rootView = (ViewGroup) inflater.inflate
                (R.layout.fragment_diary_write, container, false);
        final InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final SaveAlertDialog saveAlertDialog = new SaveAlertDialog();

        // Initialize
        init(rootView);

        // Fragment Appearence Setting
        Bundle bundle = getArguments();
//        try {
//
//        }
//        catch (NullPointerException e) {
//
//        }
        // getParcelable이 Exception을 일으킬 수 있으니까, 에러처리 해두기.
        mDiaryDateCode = bundle.getParcelable("date");
        mDiaryIdx = bundle.getString("idx");
        mIsWritten = bundle.getBoolean("isWritten");
        if (mIsWritten) {
            // TRUE : Diary has been Written.
            SQLiteDatabase loadDB = mfragDBHelper.getReadableDatabase();
            Cursor loadCursor = loadDB.query("diarylist", mFrag_colHeads, "date=? AND idx=?",
                    new String[]{mDiaryDateCode.getStrDateCode(), mDiaryIdx}, null, null, null);
            showResult(loadCursor);

            String strTitle = loadCursor.getString(loadCursor.getColumnIndex("title"));
            String strContent = loadCursor.getString(loadCursor.getColumnIndex("content"));
            mEditTextDiaryTitle.setText(strTitle);
            mEditTextContent.setText(strContent);
            loadCursor.close();
        }
        mfragDBHelper.close();

        // 받아온 날짜로 날짜 부분을 세팅합니다.
        String strDate =
                mDiaryDateCode.getStrDiaryYear()  + "." +
                mDiaryDateCode.getStrDiaryMonth() + "." +
                mDiaryDateCode.getStrDiaryDay()   + " " +
                mDiaryDateCode.getStrDayName();
        mTvDayScreen.setText(strDate);


        // SaveAlertDialog Btn Setting
        saveAlertDialog.setOnNoticeDialogListener(new SaveAlertDialog.SaveDialogListener() {
            @Override
            public void onDialogOutClick(View view) {
                setWriteMode(false);
                // 나가는 키가 추가되면서 키보드가 자동적으로 포커스를 잃는다. 이거면 대충 키보드를 없애는 기능은 가능해지지 않을까?

                mEditTextDiaryTitle.clearFocus();
                mEditTextDiaryTitle.setText(null);
                mEditTextContent.clearFocus();
                mEditTextContent.setText(null);
                mIsTextChanged = false;

                saveAlertDialog.dismiss();
            }

            @Override
            public void onDialogCancelClick(View view) {
                saveAlertDialog.dismiss();
            }
        });

        // 일기장 기록 모드에서 빠져나오는 버튼
        mImgbtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).closeDiary();
            }
        });


        // 일기장 화면에서 빠져나오는 버튼
        mImgbtnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAlertDialog.show(getFragmentManager(), SaveAlertDialog.TAG_SAVE_ALERT);

//                if (isTextChanged) {
//                    // 다이얼로그를 띄워줍시다.
//                }
//                else {
//
//                }
                // 원래 저장된 기록과 텍스트가 일치하는 방식을 해야하나?
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryInfo sendInfo = new DiaryInfo();
                sendInfo.setDbDateCode(mDiaryDateCode);

                String editedTitle = mEditTextDiaryTitle.getText().toString();
                String editedContent = mEditTextContent.getText().toString();
                if (!(editedTitle.isEmpty() && editedContent.isEmpty())) {
                    // 둘 다 비었으면 데이터 저장을 웨함? 실행할 필요 없고, 데이터 전달도 필요가 없겠죠?
                    sendInfo.setStrDiaryTitle(editedTitle);
                    sendInfo.setStrDiaryContent(editedContent);
                    sendInfo.setNumTypeCode(DIARY_ITEM);

                    ContentValues saveVal = new ContentValues();
                    SQLiteDatabase saveDB;
                    saveDB = mfragDBHelper.getWritableDatabase();
                    saveVal.put("title", mEditTextContent.getText().toString());
                    saveVal.put("content", mEditTextContent.getText().toString());
                    if (mIsWritten) {
                        // 이미 해당 날짜, 해당 IDX에 적힌 글을 불러왔었다면, 해당 부분을 Update해야한다.
                        saveDB.update("diarylist", saveVal, "date=? AND idx=?",
                                new String[]{mDiaryDateCode.getStrDateCode(), mDiaryIdx});
                    }
                    else {
                        // 아니라구요? 그럼 Insert하새오
                        saveDB.insert("diarylist", null, saveVal);
                    }
                    mfragDBHelper.close();
                }
//                else {
//                    // 근데 진자루 내용이 없으면? 뭘 처리하죠?
//                }
                setWriteMode(false);
                inputMethodManager.hideSoftInputFromWindow
                        (getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mEditTextDiaryTitle.clearFocus();
                mEditTextContent.clearFocus();
            }
        });

        mEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                setWriteMode(isFocused);
            }
        });

        mEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d("EditText", "Before Called");
                Log.d("EditText", "start : " + start);
                Log.d("EditText", "count : " + count);
                Log.d("EditText", "after : " + after);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d("EditText", "Changed Called");
                Log.d("EditText", "start : " + start);
                Log.d("EditText", "before : " + before);
                Log.d("EditText", "count : " + count);
                mIsTextChanged = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing...
                Log.d("EditText", "After Called");
            }
        });

        mEditTextDiaryTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                setWriteMode(isFocused);
            }
        });

        return rootView;
    }

    public void init(ViewGroup rootView) {

        mBtnSave = (Button) rootView.findViewById(R.id.btnSave);
        mEditTextDiaryTitle = (EditText) rootView.findViewById(R.id.editTextDiaryTitle);
        mEditTextContent = (EditText) rootView.findViewById(R.id.editTextContent);
        mImgbtnReturn = (ImageButton) rootView.findViewById(R.id.btnReturn);
        mImgbtnOut = (ImageButton) rootView.findViewById(R.id.btnOut);
        mTvDayScreen = (TextView) rootView.findViewById(R.id.tvDayScreen);
        mfragDBHelper = ((MainActivity) getActivity()).getMainDBHelper();
    }

    public void setWriteMode(boolean isWriteMode) {
        // 이 함수가 좀 이상하게 작동할수도 있으니...주의!
        if (isWriteMode) {
            mImgbtnReturn.setVisibility(View.GONE);
            mImgbtnOut.setVisibility(View.VISIBLE);
            mBtnSave.setVisibility(View.VISIBLE);
        }
        else {
            mImgbtnReturn.setVisibility(View.VISIBLE);
            mImgbtnOut.setVisibility(View.GONE);
            mBtnSave.setVisibility(View.GONE);
        }
    }

    private void showResult(Cursor cur) {
        int title_col = cur.getColumnIndex("title");
        int ctt_col = cur.getColumnIndex("content");
        while (cur.moveToNext()) {
            Log.d(LOG_TAG, cur.getString(title_col));
            Log.d(LOG_TAG, cur.getString(ctt_col));
        }
    }
}