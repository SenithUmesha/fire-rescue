package com.blackeyedghoul.firefighters;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Commends extends AppCompatActivity {

    EditText feedback, name;
    ImageView back;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commends);
        getWindow().setStatusBarColor(ContextCompat.getColor(Commends.this, R.color.dark_red));

        init();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }

                if (openMailDraft()) {
                    runAlertSuccess();
                    name.getText().clear();
                    feedback.getText().clear();
                } else {
                    runAlertFail();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            name.setError("Field can not be empty");
            valid = false;
        } else {
            name.setError(null);
        }

        if (TextUtils.isEmpty(feedback.getText().toString().trim())) {
            feedback.setError("Field can not be empty");
            valid = false;
        } else {
            feedback.setError(null);
        }

        return valid;
    }

    /**
     * The original prototype sent SMTP mail directly from the Android client.
     * The public snapshot intentionally avoids shipping or downloading mail
     * credentials. Instead it hands a pre-filled draft to the user's mail app.
     */
    private boolean openMailDraft() {
        String senderName = name.getText().toString().trim();
        String messageBody = feedback.getText().toString().trim();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Commendation / feedback from " + senderName);
        intent.putExtra(
                Intent.EXTRA_TEXT,
                "Name: " + senderName + "\n\n" + messageBody
        );

        try {
            startActivity(Intent.createChooser(intent, "Send feedback with"));
            return true;
        } catch (ActivityNotFoundException exception) {
            return false;
        }
    }

    private void runAlertSuccess() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                Commends.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.success_alert_box,
                        findViewById(R.id.cm_success_alert_box)
                );

        ColorDrawable background = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(background, 20);
        bottomSheetDialog.getWindow().setBackgroundDrawable(inset);

        TextView subTitle = bottomSheetView.findViewById(R.id.cm_success_subT);
        subTitle.setText("Your email app has been opened. Send the draft to finish.");

        bottomSheetView.findViewById(R.id.cm_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void runAlertFail() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                Commends.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.fail_alert_box,
                        findViewById(R.id.cm_fail_alert_box)
                );

        ColorDrawable background = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(background, 20);
        bottomSheetDialog.getWindow().setBackgroundDrawable(inset);

        TextView subTitle = bottomSheetView.findViewById(R.id.cm_fail_subT);
        subTitle.setText("No email app is available to open the draft.");

        bottomSheetView.findViewById(R.id.cm_close_fail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void init() {
        feedback = findViewById(R.id.cm_feedback_txt);
        name = findViewById(R.id.cm_name_txt);
        back = findViewById(R.id.cm_back);
        submit = findViewById(R.id.cm_submit);
    }
}
