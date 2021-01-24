package com.example.mytestdiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.example.mytestdiary.DiaryList.DiaryInfo;

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

    private int mDiaryIdx;
    private boolean mIsWritten;
    private boolean mIsTextChanged;
    String[] mFrag_colHeads;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mEditTextContent.isFocused() || mEditTextDiaryTitle.isFocused()) {
                    // 작성 중에는 mImgBtnOut과 동일한 기능을 수행합니다.
                    Log.d(LOG_TAG, "OUTED");
                }
                else {
                    // 아니면? Return과 동일한 기능, 즉 그냥 나가집니다.
                    Log.d(LOG_TAG, "ReturnED");
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


        // 다이어리 초기화
        mEditTextDiaryTitle.clearComposingText();
        mEditTextContent.setText(null);

        // Fragment Appearence Setting
//        try {
//
//        }
//        catch (NullPointerException e) {
//
//        }
        // getParcelable이 Exception을 일으킬 수 있으니까, 에러처리 해두기.
        Bundle bundle = getArguments();
        mDiaryDateCode = bundle.getParcelable("date");
        mDiaryIdx = bundle.getInt("idx");
        mIsWritten = bundle.getBoolean("isWritten");
        if (mIsWritten) {
            // TRUE : Diary has been Written.
            SQLiteDatabase loadDB = mfragDBHelper.getWritableDatabase();
            Cursor loadCursor;
//                    = loadDB.query("diarylist", mFrag_colHeads, "date=? and idx=?",
//                    new String[]{mDiaryDateCode.getStrDateCode(), Integer.toString(mDiaryIdx)}, null, null, null);
            loadCursor = loadDB.rawQuery("SELECT * from diarylist WHERE date = '"+mDiaryDateCode.getStrDateCode()+"' AND idx = '"+Integer.toString(mDiaryIdx)+"' ", null);
//            ((MainActivity) getActivity()).showResult(loadCursor);

            loadCursor.moveToFirst();
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
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
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

                // DB에 데이터를 저장하는 부분
                String editedTitle = mEditTextDiaryTitle.getText().toString();
                String editedContent = mEditTextContent.getText().toString();
                if (!(editedTitle.isEmpty() && editedContent.isEmpty())) {
                    // 둘 다 비었으면 데이터 저장을 웨함? 실행할 필요 없고, 데이터 전달도 필요가 없겠죠?

                    // 제목 적당히 짜르기
                    if (editedTitle.length() > 15)
                        sendInfo.setStrDiaryTitle(editedTitle.substring(0, 14));
                    else
                        sendInfo.setStrDiaryTitle(editedTitle);

                    // 내용도 적당히 짜르기
                    if (editedContent.length() > 25)
                        sendInfo.setStrDiaryContent(editedContent.substring(0, 24));
                    else
                        sendInfo.setStrDiaryContent(editedContent);

                    sendInfo.setDbDateCode(mDiaryDateCode);
                    sendInfo.setNumTypeCode(DIARY_ITEM);
                    sendInfo.setNumIdxCode(mDiaryIdx);

                    ContentValues saveVal = new ContentValues();
                    SQLiteDatabase saveDB = mfragDBHelper.getWritableDatabase();
                    saveVal.put("title", editedTitle);
                    saveVal.put("content", editedContent);
                    if (mIsWritten) {
                        // 이미 해당 날짜, 해당 IDX에 적힌 글을 불러왔었다면, 해당 부분을 Update 해야 한다.
                        saveDB.update("diarylist", saveVal, "date=? AND idx=?",
                                new String[]{mDiaryDateCode.getStrDateCode(), Integer.toString(mDiaryIdx)});
                    } else {
                        // 아니라구요? 그럼 Insert하새오
                        saveVal.put("date", mDiaryDateCode.getNumDateCode());
                        saveVal.put("idx", mDiaryIdx);
                        saveVal.put("dayname", mDiaryDateCode.getStrDayName());
                        saveDB.insert("diarylist", null, saveVal);
                    }
                    mfragDBHelper.close();

                    // 리스트에 삽입하기
                    ((MainActivity) getActivity()).setListItem(sendInfo);
                }
//                else {
//                    // 근데 진자루 내용이 없으면? 뭘 처리하죠?
//                }


                // 저장 후 마지막 후처리
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
//                Log.d("EditText", "Before Called");
//                Log.d("EditText", "start : " + start);
//                Log.d("EditText", "count : " + count);
//                Log.d("EditText", "after : " + after);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                Log.d("EditText", "Changed Called");
//                Log.d("EditText", "start : " + start);
//                Log.d("EditText", "before : " + before);
//                Log.d("EditText", "count : " + count);
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
        mEditTextDiaryTitle = rootView.findViewById(R.id.editTextDiaryTitle);
        mEditTextContent = rootView.findViewById(R.id.editTextContent);
        mImgbtnReturn = rootView.findViewById(R.id.btnReturn);
        mImgbtnOut = (ImageButton) rootView.findViewById(R.id.btnOut);
        mTvDayScreen = (TextView) rootView.findViewById(R.id.tvDayScreen);
        mfragDBHelper = ((MainActivity) getActivity()).getMainDBHelper();

        mFrag_colHeads = getResources().getStringArray(R.array.all_column_names);
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
        int date = cur.getColumnIndex("date");
        int idx = cur.getColumnIndex("idx");
        int title_col = cur.getColumnIndex("title");
        int ctt_col = cur.getColumnIndex("content");
        while (cur.moveToNext()) {
            Log.d(LOG_TAG, "Now In DB : ");
            Log.d(LOG_TAG, "date : " + cur.getString(date));
            Log.d(LOG_TAG, "idx : " + cur.getString(idx));
            Log.d(LOG_TAG, "Title : " + cur.getString(title_col));
            Log.d(LOG_TAG, "Content : " + cur.getString(ctt_col));
        }
    }
}