package com.example.mytestdiary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SaveAlertDialog extends DialogFragment {

    public static final String TAG_SAVE_ALERT = "dialog_saveAlert";

    private Button btnDlgOut;
    private Button btnDlgCancel;
    private SaveDialogListener m_Listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.dialog_save_alert, null);
        setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnDlgOut = (Button) mainView.findViewById(R.id.btnDlgOut);
        btnDlgOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_Listener != null) {
                    m_Listener.onDialogOutClick(view);
                }
            }
        });

        btnDlgCancel = (Button) mainView.findViewById(R.id.btnDlgCancel);
        btnDlgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_Listener != null) {
                    m_Listener.onDialogCancelClick(view);
                }
            }
        });
        return mainView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public interface SaveDialogListener {
        void onDialogOutClick(View view);
        void onDialogCancelClick(View view);
    }

    public void setOnNoticeDialogListener(SaveDialogListener m_Listener) {
        this.m_Listener = m_Listener;
    }
}
