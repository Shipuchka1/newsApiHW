package com.example.newsapihw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread;
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("https://newsapi.org/v2/everything?q=bitcoin&from=2019-04-10&language=ru&apiKey=11b9c634800d4700ae647ca4d9b7b4dd");
                    HttpURLConnection con = null;
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    int status = con.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    con.disconnect();


                    final SomeNew p = new SomeNew();
                    JSONObject reader = new JSONObject(content.toString());
                    p.source = reader.getJSONArray("articles").getJSONObject(0).getJSONObject("source").getString("name");
                    p.url = reader.getJSONArray("articles").getJSONObject(0).getString("url");
                    p.title = reader.getJSONArray("articles").getJSONObject(0).getString("title");
                    p.urlToImage = reader.getJSONArray("articles").getJSONObject(0).getString("urlToImage");


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                             TextView title = (TextView) findViewById(R.id.title);
                            TextView url = (TextView) findViewById(R.id.url);
                            ImageView image = (ImageView) findViewById(R.id.image);
                            TextView source = (TextView) findViewById(R.id.source);

                            title.setText(p.title);
                            url.setText(p.url);
                            source.setText(p.source);
                            Glide
                                    .with(MainActivity.this)
                                    .load(p.urlToImage)
                                    .asGif()
                                    .into(image);
                        }
                    });


                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});
        thread.start();
    }
}
