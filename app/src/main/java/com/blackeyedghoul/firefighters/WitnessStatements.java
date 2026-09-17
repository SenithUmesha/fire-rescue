package com.blackeyedghoul.firefighters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WitnessStatements extends AppCompatActivity {

    ImageView back, erase;
    EditText dateAndTime, dob, email, name, add_notes;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witness_statements);
        getWindow().setStatusBarColor(ContextCompat.getColor(WitnessStatements.this, R.color.dark_red));

        init();

        dateAndTime.setInputType(InputType.TYPE_NULL);
        dob.setInputType(InputType.TYPE_NULL);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateAndTimeDialog(dateAndTime);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDOBDialog(dob);
            }
        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForm();
                Toast.makeText(WitnessStatements.this, "Cleared!", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateName() | !validateDOB() | !validateDAT() | !validateEmail()) {
                    return;
                }

                if (openMailDraft()) {
                    runAlertSuccess();
                    clearForm();
                } else {
                    runAlertFail();
                }
            }
        });
    }

    /**
     * This historical project originally authenticated directly to SMTP from
     * the Android client. The public snapshot intentionally does not handle
     * mail credentials. A pre-filled draft is handed to an installed mail app
     * instead, leaving delivery and account authentication outside the app.
     */
    private boolean openMailDraft() {
        String witnessName = name.getText().toString().trim();
        String witnessDob = dob.getText().toString().trim();
        String incidentDate = dateAndTime.getText().toString().trim();
        String contactEmail = email.getText().toString().trim();
        String notes = add_notes.getText().toString().trim();

        StringBuilder body = new StringBuilder()
                .append("Name: ").append(witnessName)
                .append("\nD.O.B: ").append(witnessDob)
                .append("\nDate & Time: ").append(incidentDate);

        if (!contactEmail.isEmpty()) {
            body.append("\nContact Email: ").append(contactEmail);
        }

        if (!notes.isEmpty()) {
            body.append("\nAdditional Notes: ").append(notes);
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Witness statement: " + witnessName);
        intent.putExtra(Intent.EXTRA_TEXT, body.toString());

        try {
            startActivity(Intent.createChooser(intent, "Send witness statement with"));
            return true;
        } catch (ActivityNotFoundException exception) {
            return false;
        }
    }

    private void runAlertSuccess() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                WitnessStatements.this, R.style.BottomSheetDialogTheme
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
                WitnessStatements.this, R.style.BottomSheetDialogTheme
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

    private void clearForm() {
        dateAndTime.getText().clear();
        dob.getText().clear();
        name.getText().clear();
        add_notes.getText().clear();
        email.getText().clear();
    }

    private boolean validateEmail() {
        String val = email.getText().toString();
        String checkForLetters = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (!val.isEmpty() && !val.matches(checkForLetters)) {
            email.setError("Invalid email!");
            return false;
        }

        email.setError(null);
        return true;
    }

    private boolean validateDAT() {
        String val = dateAndTime.getText().toString();

        if (val.isEmpty()) {
            dateAndTime.setError("Field can not be empty");
            return false;
        }

        dateAndTime.setError(null);
        return true;
    }

    private boolean validateDOB() {
        String val = dob.getText().toString();

        if (val.isEmpty()) {
            dob.setError("Field can not be empty");
            return false;
        }

        dob.setError(null);
        return true;
    }

    private boolean validateName() {
        String val = name.getText().toString();

        if (val.isEmpty()) {
            name.setError("Field can not be empty");
            return false;
        }

        name.setError(null);
        return true;
    }

    private void showDOBDialog(EditText dob) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
                dob.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(
                WitnessStatements.this,
                R.style.DialogTheme,
                onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showDateAndTimeDialog(EditText dateAndTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
                        dateAndTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(
                        WitnessStatements.this,
                        R.style.DialogTheme,
                        timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false
                ).show();
            }
        };

        new DatePickerDialog(
                WitnessStatements.this,
                R.style.DialogTheme,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    public void init() {
        dateAndTime = findViewById(R.id.ws_date_time_txt);
        dob = findViewById(R.id.ws_dob_txt);
        back = findViewById(R.id.ws_back);
        add_notes = findViewById(R.id.ws_ad_notes_txt);
        name = findViewById(R.id.ws_name_txt);
        email = findViewById(R.id.ws_email_txt);
        submit = findViewById(R.id.ws_submit);
        erase = findViewById(R.id.ws_erase);
    }
}
