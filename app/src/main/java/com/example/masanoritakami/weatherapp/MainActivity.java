package com.example.masanoritakami.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    TextView mTitle;
    TextView mDateLabel0;
    TextView mDateLabel1;
    TextView mTelop0;
    TextView mTelop1;
    TextView mDescription;
    NetworkImageView mImage0;
    NetworkImageView mImage1;
    ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = (TextView)findViewById(R.id.title);
        mDateLabel0 = (TextView)findViewById(R.id.dateLabel0);
        mDateLabel1 = (TextView)findViewById(R.id.dateLabel1);
        mTelop0 = (TextView)findViewById(R.id.telop0);
        mTelop1 = (TextView)findViewById(R.id.telop1);
        mDescription = (TextView)findViewById(R.id.description);
        mImage0 = (NetworkImageView)findViewById(R.id.image0);
        mImage1 = (NetworkImageView)findViewById(R.id.image1);
        mImageLoader = MySingleton.getInstance(this).getImageLoader();

        String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=130010";

        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            Log.d(TAG, response.toString(2));
                            mTitle.setText(response.getString("title"));
                            mDescription.setText(response.getJSONObject("description").getString("text"));
                            mDateLabel0.setText(response.getJSONArray("forecasts").getJSONObject(0).getString("date"));
                            mDateLabel1.setText(response.getJSONArray("forecasts").getJSONObject(1).getString("date"));
                            mTelop0.setText(response.getJSONArray("forecasts").getJSONObject(0).getString("telop"));
                            mTelop1.setText(response.getJSONArray("forecasts").getJSONObject(1).getString("telop"));
                            String url0 = response.getJSONArray("forecasts").getJSONObject(0).getJSONObject("image").getString("url");
                            mImage0.setImageUrl(url0, mImageLoader);
                            String url1 = response.getJSONArray("forecasts").getJSONObject(1).getJSONObject("image").getString("url");
                            mImage1.setImageUrl(url1, mImageLoader);
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(TAG, error.toString());
                    }
                }
        );
        MySingleton.getInstance(this).addToRequestQueue(jsObjectRequest);
    }
}
