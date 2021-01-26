package com.example.pma_project;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog picker;
    private EditText editTextDate;
    private Button buttonSelectDate;
    private TextView viewTitle;
    private TextView viewDescription;
    private ImageView imageView;
    private View view;
    private Context context;
    private String imageURL;
    private String date;
    private String pathToImage;

    public static ArrayList<Data> data = new ArrayList<>();
    final static String appDir = "/AstronomyPictures/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDate=(EditText) findViewById(R.id.editTextDate);
        editTextDate.setInputType(InputType.TYPE_NULL);
        buttonSelectDate = (Button)findViewById(R.id.buttonSelectDate);
        viewTitle = (TextView)findViewById(R.id.textViewTitle);
        viewDescription = (TextView)findViewById(R.id.textViewDescription);
        imageView = (ImageView)findViewById(R.id.imageView);
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

                                date = jsonObject.getString("date");
                                String title = jsonObject.getString("title");
                                String description = jsonObject.getString("explanation");
                                imageURL = jsonObject.getString("url");

                                pathToImage = Environment.getExternalStorageDirectory() + appDir + date + ".jpeg";
                                if(imageURL != null)
                                downloadImageFromUrl(v);

                                viewTitle.setText(title);
                                viewDescription.setText(description);
                            }
                            catch (JSONException e)
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
                            Toast.makeText(getApplicationContext(),"We can't see the future nor NASA, sorry :(", Toast.LENGTH_LONG).show();
                        }
                    });
            queue.add(stringRequest);
    }

    public void downloadImageFromUrl(View view) {
            MyAsyncTask asyncTask = new MyAsyncTask();
            asyncTask.execute();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(imageURL);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                File SDCardRoot = Environment.getExternalStorageDirectory();

                File directory = new File(SDCardRoot + appDir);
                if(!directory.exists()) {
                    directory.mkdir();
                }

                File file = new File(SDCardRoot,appDir + date + ".jpeg");

                FileOutputStream fileOutput = new FileOutputStream(file);

                InputStream inputStream = urlConnection.getInputStream();

                int totalSize = urlConnection.getContentLength();

                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer
                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;
                    //this is where you would do something to report the progress
                    //updateProgress(downloadedSize, totalSize);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Image Downloaded to sd card",Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(BitmapFactory.decodeFile(pathToImage));
        }
    }

    public void onHistoryClick(View v)
    {
        Intent intent = new Intent(context, HistoryActivity.class);
        startActivity(intent);
    }

    public void storeData(String date, String title, String explanation, String pathToImage)
    {

    }
}