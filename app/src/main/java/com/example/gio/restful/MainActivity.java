package com.example.gio.restful;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://jsonparsing.parseapp.com/jsonData/moviesDemoList.txt";
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Ok = (Button) findViewById(R.id.OK);
        result = (TextView) findViewById(R.id.result);
        result.setText("");

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetJsonBasa().execute();
            }
        });
    }

    public class GetJsonBasa extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer;

            try {
                URL url = new URL(URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("movies");

                String[] movieName = new String[parentArray.length()];

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalOject = parentArray.getJSONObject(i);

                    movieName[i] = finalOject.getString("movie") + " - " + finalOject.getInt("year");
                }

                return movieName;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] stringBuffer) {
            super.onPostExecute(stringBuffer);
            for (String string : stringBuffer)
                result.setText(result.getText().toString() + string + "\n");
        }
    }
}
