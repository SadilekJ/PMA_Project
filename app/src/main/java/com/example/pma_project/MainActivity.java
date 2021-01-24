package com.example.pma_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog picker;
    EditText editTextDate;
    Button buttonSelectDate;
    TextView viewTitle;
    TextView viewDescription;
    ImageView imageView;
    View view;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDate=(EditText) findViewById(R.id.editTextDate);
        editTextDate.setInputType(InputType.TYPE_NULL);
        buttonSelectDate = (Button)findViewById(R.id.buttonSelectDate);
        viewTitle = (TextView)findViewById(R.id.textViewTitle);
        viewDescription = (TextView)findViewById(R.id.textViewDescription);
        context = this;

//        NavHostFragment navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment);
//        NavController navController = navHostFragment.getNavController();
//        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
//        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    public void onEditClick(View v)
    {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        picker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        if((monthOfYear >= 1 && monthOfYear <= 9) && (dayOfMonth >= 1 && dayOfMonth <= 9))
                        {
                            editTextDate.setText(year + "-" + "0" + monthOfYear + "-" + "0" + dayOfMonth);
                        }
                        else if((dayOfMonth >= 1 && dayOfMonth <= 9) && (monthOfYear > 9))
                        {
                            editTextDate.setText(year + "-" + monthOfYear + "-" + "0" + dayOfMonth);
                        }
                        else if((monthOfYear >= 1 && monthOfYear <= 9) && (dayOfMonth > 9))
                        {
                            editTextDate.setText(year + "-" + "0" + monthOfYear + "-" + dayOfMonth);
                        }
                        else
                        editTextDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, year, month, day);
        picker.show();
    }

    public void onButtonSelectDateClick(View v)
    {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url ="https://api.nasa.gov/planetary/apod?api_key=m9enue7ZctoE5BvVOGhhJffrh3v59cG5hXJau5Nj" + "&date=" + editTextDate.getText().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(response);

                                String title = jsonObject.getString("title");
                                String description = jsonObject.getString("explanation");
                                String imageUrl = jsonObject.getString("url");

                                URI imageUri = new URI(imageUrl);

                                viewTitle.setText(title);
                                viewDescription.setText(description);
                            }
                            catch (JSONException | URISyntaxException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            viewTitle.setText("That didn't work!");
                        }
                    });
            queue.add(stringRequest);
    }


}