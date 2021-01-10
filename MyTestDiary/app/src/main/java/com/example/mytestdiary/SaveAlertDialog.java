package com.example.mytestdiary;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SaveAlertDialog {

    private Context context;

    public SaveAlertDialog(Context context) {
        this.context = context;
    }

    public void callDialog() {
        // 액티비티 불러오기
       final Dialog dialog = new Dialog(context);
       Button btnOut = (Button) dialog.findViewById(R.id.btnDlgOut);
       Button btnCancel = (Button) dialog.findViewById(R.id.btnDlgCancel);

       // 타이틀 바 숨기기
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

       // 이 다이얼로그의 레이아웃 설정
        dialog.setContentView(R.layout.dialog_save_alert);

        // dialog SHOW
        dialog.show();

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
