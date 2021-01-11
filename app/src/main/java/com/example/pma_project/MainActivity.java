package com.example.pma_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog picker;
    EditText editTextDate;
    Button buttonSelectDate;
    TextView viewTitle;
    TextView viewDescription;
    VideoView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //viewDescription = (TextView) findViewById(R.id.textViewDescription);
        //viewDescription.setMovementMethod(new ScrollingMovementMethod());

        editTextDate=(EditText) findViewById(R.id.editTextDate);
        editTextDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editTextDate.setText(year + "-" + (monthOfYear + 1) + "-" + day);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        buttonSelectDate = (Button)findViewById(R.id.buttonSelectDate);
        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}