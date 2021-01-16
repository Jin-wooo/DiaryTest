package com.example.mytestdiary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;

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

    // Android`s
    private Button btnSave;
    private EditText editTextContent;
    private EditText editTextDiaryTitle;
    private ImageButton imgbtnReturn;
    private ImageButton imgbtnOut;
    private TextView tvDayScreen;

    private DiaryDBHelper fragmentDBHelper;
    private DBDateCode diaryDateCode;

    private boolean isTextChanged;
    String[] frag_colHeads = {"date", "idx", "title", "content"};



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
//        final InputMethodManager inputMethodManager =
//                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final SaveAlertDialog saveAlertDialog = new SaveAlertDialog();

        // Initialize
        init(rootView);

        // Fragment Appearence Setting

        Bundle bundle = getArguments();
        diaryDateCode = bundle.getParcelable("date");
        String diaryIdx = bundle.getString("idx");
        if (bundle.getBoolean("isWritten")) {
            // TRUE : Diary has been Written.
            SQLiteDatabase loadDB = fragmentDBHelper.getReadableDatabase();
            Cursor loadCursor = loadDB.query("diarylist", frag_colHeads, "date=? AND idx=?",
                    new String[]{diaryDateCode.getStrDateCode(), diaryIdx}, null, null, null);
            loadCursor.close();
        }
        fragmentDBHelper.close();

        String strDate =
                diaryDateCode.getStrDiaryYear()  + "." +
                diaryDateCode.getStrDiaryMonth() + "." +
                diaryDateCode.getStrDiaryDay()   + " " +
                diaryDateCode.getStrDayName();
        tvDayScreen.setText(strDate);






        // SaveAlertDialog Btn Setting
        saveAlertDialog.setOnNoticeDialogListener(new SaveAlertDialog.SaveDialogListener() {
            @Override
            public void onDialogOutClick(View view) {
                imgbtnReturn.setVisibility(View.VISIBLE);
                imgbtnOut.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                // 나가는 키가 추가되면서 키보드가 자동적으로 포커스를 잃는다. 이거면 대충 키보드를 없애는 기능은 가능해지지 않을까?
//                        inputMethodManager.hideSoftInputFromWindow
//                                (getActivity().getCurrentFocus().getWindowToken(), inputMethodManager.HIDE_NOT_ALWAYS);
                editTextContent.clearFocus();
                editTextDiaryTitle.clearFocus();
                isTextChanged = false;

                saveAlertDialog.dismiss();
            }

            @Override
            public void onDialogCancelClick(View view) {
                saveAlertDialog.dismiss();
            }
        });

        imgbtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.closeDiary(); }
        });

        imgbtnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAlertDialog.show(getFragmentManager(), SaveAlertDialog.TAG_SAVE_ALERT);

//                if (isTextChanged) {
//                    // 다이얼로그를 띄워줍시다.
//                }
//                else {
//
//                }
            }
        });

        btnSave = (Button) rootView.findViewById(R.id.btnSave);



        editTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused) {
                    imgbtnReturn.setVisibility(View.GONE);
                    imgbtnOut.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                }
                else {

                }
            }
        });

        editTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d("EditText", "Before Called");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d("EditText", "Changed Called");
                isTextChanged = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing...
                Log.d("EditText", "After Called");
            }
        });

        editTextDiaryTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused) {
                    imgbtnReturn.setVisibility(View.GONE);
                    imgbtnOut.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }

    public void init(ViewGroup rootView) {

        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        editTextDiaryTitle = (EditText) rootView.findViewById(R.id.editTextDiaryTitle);
        editTextContent = (EditText) rootView.findViewById(R.id.editTextContent);
        imgbtnReturn = (ImageButton) rootView.findViewById(R.id.btnReturn);
        imgbtnOut = (ImageButton) rootView.findViewById(R.id.btnOut);
        tvDayScreen = (TextView) rootView.findViewById(R.id.tvDayScreen);
        fragmentDBHelper = new DiaryDBHelper(getActivity());
    }

}