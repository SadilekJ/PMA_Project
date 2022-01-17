package com.example.pma_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.graphics.BitmapFactory
import android.content.Context
import android.os.AsyncTask
import android.content.Intent
import android.os.Environment
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.google.gson.Gson
import kotlin.Throws
import com.google.gson.reflect.TypeToken
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import org.json.JSONException
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    private var picker: DatePickerDialog? = null
    private var editTextDate: EditText? = null
    private var buttonSelectDate: Button? = null
    private var viewTitle: TextView? = null
    private var viewDescription: TextView? = null
    private var imageView: ImageView? = null
    private val view: View? = null
    private var context: Context? = null
    private var imageURL: String? = null
    private var date: String? = null
    private var pathToImage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextDate = findViewById<View>(R.id.editTextDate) as EditText
        editTextDate!!.inputType = InputType.TYPE_NULL
        buttonSelectDate = findViewById<View>(R.id.buttonSelectDate) as Button
        viewTitle = findViewById<View>(R.id.textViewTitle) as TextView
        viewDescription = findViewById<View>(R.id.textViewDescription) as TextView
        imageView = findViewById<View>(R.id.imageView) as ImageView
        context = this
        if (data.size == 0) {
            if (appFileExists(fullPathToFile)) {
                try {
                    loadData()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if (intent.extras != null) {
            val id = intent.extras!!.getInt("position")
            if (0 <= id && id < data.size) {
                val dataToShow = data[id]
                imageView!!.setImageBitmap(BitmapFactory.decodeFile(dataToShow.pathToImage))
                viewTitle!!.text = dataToShow.title
                viewDescription!!.text = dataToShow.explanation
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun onEditClick(v: View?) {
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        picker = DatePickerDialog(context!!,
                { view, yearO, monthOfYear, dayOfMonth ->
                    var monthOfYear = monthOfYear
                    monthOfYear += 1
                    if (monthOfYear in 1..9 && dayOfMonth in 1..9) {
                        editTextDate!!.setText("$yearO-0$monthOfYear-0$dayOfMonth")
                    } else if (dayOfMonth in 1..9 && monthOfYear > 9) {
                        editTextDate!!.setText("$yearO-$monthOfYear-0$dayOfMonth")
                    } else if (monthOfYear in 1..9 && dayOfMonth > 9) {
                        editTextDate!!.setText("$yearO-0$monthOfYear-$dayOfMonth")
                    } else editTextDate!!.setText("$yearO-$monthOfYear-$dayOfMonth")
                }, year, month, day)
        picker!!.show()
    }

    fun onButtonSelectDateClick(v: View?) {
        val inputDate = editTextDate!!.text.toString()
        val i = checkIfSelectedDateExists(inputDate)
        if (i != -1) {
            val dataToShow = data[i]
            imageView!!.setImageBitmap(BitmapFactory.decodeFile(dataToShow.pathToImage))
            viewTitle!!.text = dataToShow.title
            viewDescription!!.text = dataToShow.explanation
        } else getApiResponse(v)
    }

    private fun downloadImageFromUrl(view: View?) {
        val asyncTask = MyAsyncTask()
        asyncTask.execute()
    }

    internal inner class MyAsyncTask : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg params: Void?): Void? {
            try {
                val url = URL(imageURL)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.doOutput = true
                urlConnection.connect()
                val sDCardRoot = Environment.getExternalStorageDirectory()
                val directory = File(sDCardRoot.toString() + appDir)
                if (!directory.exists()) {
                    directory.mkdir()
                }
                val file = File(sDCardRoot, "$appDir$date.jpeg")
                val fileOutput = FileOutputStream(file)
                val inputStream = urlConnection.inputStream
                //val totalSize = urlConnection.contentLength
                var downloadedSize = 0
                val buffer = ByteArray(1024)
                var bufferLength: Int //used to store a temporary size of the buffer
                //now, read through the input buffer and write the contents to the file
                while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength)
                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength
                    //this is where you would do something to report the progress
                    //updateProgress(downloadedSize, totalSize);
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            Toast.makeText(applicationContext, "Image Downloaded to sd card", Toast.LENGTH_SHORT).show()
            imageView!!.setImageBitmap(BitmapFactory.decodeFile(pathToImage))
        }

    }

    fun onHistoryClick(v: View?) {
        val intent = Intent(context, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun storeData(dataToStore: Data) {
        val gson = Gson()
        val dataToJson = gson.toJson(dataToStore)
        if (appFileExists(fullPathToFile)) {
            try {
                val stream = ",$dataToJson"
                val fos = FileOutputStream(fullPathToFile, true)
                fos.write(stream.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                val fos = FileOutputStream(fullPathToFile, false)
                fos.write(dataToJson.toByteArray())
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        data.add(dataToStore)
    }

    @Throws(IOException::class)
    fun loadData() {
        val gson = Gson()
        val br = BufferedReader(FileReader(fullPathToFile))
        var line: String? = ""
        var dataFromFile: String? = "["
        while (br.readLine().also { line = it } != null) {
            dataFromFile += line
        }
        dataFromFile += "]"
        val dataListType = object : TypeToken<ArrayList<Data?>?>() {}.type
        data = gson.fromJson(dataFromFile, dataListType)
    }

    private fun appFileExists(fullPathToFile: String): Boolean {
        val file = File(fullPathToFile)
        return file.exists()
    }

    @SuppressLint("SetTextI18n")
    private fun getApiResponse(v: View?) {
        val queue = Volley.newRequestQueue(context)
        val url = "https://api.nasa.gov/planetary/apod?api_key=m9enue7ZctoE5BvVOGhhJffrh3v59cG5hXJau5Nj" + "&date=" + editTextDate!!.text.toString()
        val stringRequest = StringRequest(Request.Method.GET, url,
                { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        date = jsonObject.getString("date")
                        val title = jsonObject.getString("title")
                        val explanation = jsonObject.getString("explanation")
                        imageURL = jsonObject.getString("url")
                        pathToImage = Environment.getExternalStorageDirectory().toString() + appDir + date + ".jpeg"
                        if (imageURL != null) downloadImageFromUrl(v)
                        viewTitle!!.text = title
                        viewDescription!!.text = explanation
                        val dataToStore = Data(date, pathToImage!!, title, explanation)
                        storeData(dataToStore)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
        ) {
            viewTitle!!.text = "That didn't work!"
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
        }
        queue.add(stringRequest)
    }

    private fun checkIfSelectedDateExists(inputDate: String): Int {
        var i = 0
        for (data in data) {
            run {
                if (data.date == inputDate) {
                    return i
                }
                i++
            }
        }
        return -1
    }

    companion object {
        @JvmField
        var data = ArrayList<Data>()
        const val appDir = "/AstronomyPictures/"
        private const val appDataFileName = "appData.txt"
        private val pathToStorage = Environment.getExternalStorageDirectory().absolutePath
        val fullPathToFile = pathToStorage + appDir + appDataFileName
    }
}