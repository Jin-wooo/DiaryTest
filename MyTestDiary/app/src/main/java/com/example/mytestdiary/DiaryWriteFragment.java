package com.example.mytestdiary;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

import javax.microedition.khronos.egl.EGLDisplay;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryWriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryWriteFragment extends Fragment {

    private EditText editTextContent;
    private EditText editTextDiaryTitle;
    private ImageButton imgbtnReturn;
    private ImageButton imgbtnOut;
    private Button btnSave;

    private boolean isTextChanged;

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


        imgbtnReturn = (ImageButton) rootView.findViewById(R.id.btnReturn);
        imgbtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.closeDiary(); }
        });

        imgbtnOut = (ImageButton) rootView.findViewById(R.id.btnOut);
        imgbtnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgbtnReturn.setVisibility(View.VISIBLE);
                imgbtnOut.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                inputMethodManager.hideSoftInputFromWindow
                        (getActivity().getCurrentFocus().getWindowToken(), inputMethodManager.HIDE_NOT_ALWAYS);
                editTextContent.clearFocus();
                editTextDiaryTitle.clearFocus();

                saveAlertDialog.setOnNoticeDialogListener(new SaveAlertDialog.SaveDialogListener() {
                    @Override
                    public void onDialogOutClick(View view) {

                    }

                    @Override
                    public void onDialogCancelClick(View view) {
                        saveAlertDialog.dismiss();
                    }
                });
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


        editTextContent = (EditText) rootView.findViewById(R.id.editTextContent);
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
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing...
                Log.d("EditText", "After Called");
            }
        });

        editTextDiaryTitle = (EditText) rootView.findViewById(R.id.editTextDiaryTitle);
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
}