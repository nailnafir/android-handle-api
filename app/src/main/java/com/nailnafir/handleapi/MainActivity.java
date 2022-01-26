package com.nailnafir.handleapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView tvShow;
    private Button buttonRefresh;

    private String apiUrl = "https://api.kawalcoron.com/indonesia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        getDataHome();
    }

    private void getDataHome() {
        getHomeAsyncTask getHomeAsyncTask = new getHomeAsyncTask();
        getHomeAsyncTask.execute();
    }

    private void initListener() {
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataHome();
            }
        });
    }

    private void initView() {
        tvShow = findViewById(R.id.tv_show);
        buttonRefresh = findViewById(R.id.button_refresh);
    }

    public class getHomeAsyncTask extends AsyncTask<String, String, String> {
        // sebelum dieksekusi
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Harap tunggu....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);

                    int data = in.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        // kalo udah selesai diekskusi
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject object = jsonArray.getJSONObject(0);
                String showData = "Nama Negara: " + object.getString("name");

                tvShow.setText(showData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}