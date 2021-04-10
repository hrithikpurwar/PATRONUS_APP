package com.example.contacttracer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static int timeout=1900;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if(ParseUser.getCurrentUser()!=null){
                    ParseQuery<ParseObject> query=ParseQuery.getQuery("data");
                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects.size()==0 || objects==null){
                                Intent intent = new Intent(getApplicationContext(),details.class);
                                startActivity(intent);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                MainActivity.this.finish();
                            }
                            else{
                                Intent intent = new Intent(getApplicationContext(),userInterface.class);
                                startActivity(intent);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                MainActivity.this.finish();

                            }
                        }
                    });
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    MainActivity.this.finish();

                }
            }
        },timeout);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}