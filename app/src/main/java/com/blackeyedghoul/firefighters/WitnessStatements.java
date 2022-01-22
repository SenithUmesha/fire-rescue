package com.blackeyedghoul.firefighters;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
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

import com.airbnb.lottie.L;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
                dateAndTime.getText().clear();
                dob.getText().clear();
                name.getText().clear();
                add_notes.getText().clear();
                email.getText().clear();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateName() | !validateDOB() | !validateDAT() | !validateEmail()) {
                    return;
                }

                if (MainActivity.isConnected(WitnessStatements.this)) {

                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                sendMail();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

                    runAlertSuccess();
                } else {
                    runAlertFail();
                }

                dateAndTime.getText().clear();
                dob.getText().clear();
                name.getText().clear();
                add_notes.getText().clear();
                email.getText().clear();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @SuppressLint("SetTextI18n")
    private void runAlertSuccess() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                WitnessStatements.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.success_alert_box,
                        findViewById(R.id.cm_success_alert_box)
                );

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        bottomSheetDialog.getWindow().setBackgroundDrawable(inset);

        TextView subT = bottomSheetView.findViewById(R.id.cm_success_subT);
        subT.setText("Your report has been sent.");

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

    @SuppressLint("SetTextI18n")
    private void runAlertFail() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                WitnessStatements.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.fail_alert_box,
                        findViewById(R.id.cm_fail_alert_box)
                );

        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        bottomSheetDialog.getWindow().setBackgroundDrawable(inset);

        TextView subT = bottomSheetView.findViewById(R.id.cm_fail_subT);
        subT.setText("Report submission failed.");

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

    private void sendMail() {

        String val = email.getText().toString();

        if (!val.isEmpty()) {

            String mail = MainActivity.sending_mail;
            String password = MainActivity.sending_password;
            String body = "Name: " + name.getText().toString() + "\nD.O.B: " + dob.getText().toString() + "\nDate & Time: " + dateAndTime.getText().toString() + "\nAdditional Notes: " + add_notes.getText().toString() + "\nEmail: " + email.getText().toString();
            String subject = "Witness Statements: " + name.getText().toString();

            Properties properties = new Properties();

            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.starttls.enable", true);
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            Session session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(mail, password);
                        }
                    });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getText().toString()));
                message.setSubject(subject);
                message.setText(body);
                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        String mail = "senithumeshac@gmail.com";
        String password = "senithumeshac#";
        String body = "Name: " + name.getText().toString() + "\nD.O.B: " + dob.getText().toString() + "\nDate & Time: " + dateAndTime.getText().toString() + "\nAdditional Notes: " + add_notes.getText().toString() + "\nEmail: " + email.getText().toString();
        String subject = "Witness Statements: " + name.getText().toString();

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mail, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("34senith@gmail.com"));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private boolean validateEmail() {
        String val = email.getText().toString();
        String checkForLetters = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (!val.isEmpty()) {
            if (!val.matches(checkForLetters)) {
                email.setError("Invalid email!");
                return false;
            }
        }
        email.setError(null);
        return true;
    }

    private boolean validateDAT() {
        String val = dateAndTime.getText().toString();

        if (val.isEmpty()) {
            dateAndTime.setError("Field can not be empty");
            return false;
        } else {
            dateAndTime.setError(null);
            return true;
        }
    }

    private boolean validateDOB() {
        String val = dob.getText().toString();

        if (val.isEmpty()) {
            dob.setError("Field can not be empty");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String val = name.getText().toString();

        if (val.isEmpty()) {
            name.setError("Field can not be empty");
            return false;
        } else {
            name.setError(null);
            return true;
        }
    }

    private void showDOBDialog(EditText dob) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
                dob.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(WitnessStatements.this, R.style.DialogTheme, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showDateAndTimeDialog(EditText dateAndTime) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");

                        dateAndTime.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(WitnessStatements.this, R.style.DialogTheme, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(WitnessStatements.this, R.style.DialogTheme, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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