package com.anhnt.kovidict.myapplication.dialog;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.anhnt.kovidict.myapplication.MainActivity;
import com.anhnt.kovidict.myapplication.R;

public class InfoDialog extends Dialog {
    private MainActivity main;
    private TextView info;
    private Button btnOk;
    private View.OnClickListener onClickListener;

    public InfoDialog(MainActivity mainActivity) {
        super(mainActivity);
        this.onClickListener = new View.OnClickListener() {
            final InfoDialog infoDialog;
            {
                this.infoDialog = InfoDialog.this;
            }
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_ok:
                        this.infoDialog.dismiss();
                        break;
                }
            }
        };

        this.main = mainActivity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_info);
        this.btnOk = (Button) findViewById(R.id.btn_ok);

        this.btnOk.setOnClickListener(this.onClickListener);
    }

}
