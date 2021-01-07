package com.example.mytestdiary;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiaryWriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryWriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton imgbtnReturn;
    private ImageButton imgbtnOut;
    private Button btnSave;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiaryWriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryWriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryWriteFragment newInstance(String param1, String param2) {
        DiaryWriteFragment fragment = new DiaryWriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final ViewGroup rootView = (ViewGroup) inflater.inflate
                (R.layout.fragment_diary_write, container, false);

        imgbtnReturn = (ImageButton) rootView.findViewById(R.id.btnReturn);
        imgbtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.closeDiary(); }
        });

        EditText editTextContent = (EditText) rootView.findViewById(R.id.editTextContent);
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


        return rootView;
    }
}