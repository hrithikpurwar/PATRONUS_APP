package com.example.contacttracer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    String username,pwd;
    TextView username_Text,password_Text,create;

    public void login(View view){

        username_Text=(TextView) findViewById(R.id.username);
        password_Text=(TextView)findViewById(R.id.password);
        username=username_Text.getText().toString();
        pwd=password_Text.getText().toString();
        ParseUser.logInInBackground(username, pwd, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user!=null ){
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();

                    ParseQuery<ParseObject> query=ParseQuery.getQuery("data");
                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(objects.size()==0 || objects==null){
                                Intent intent = new Intent(getApplicationContext(),details.class);
//                                TaskStackBuilder.create(getApplicationContext()).addNextIntentWithParentStack(intent).startActivities();
                                startActivity(intent);
                                HomeActivity.this.finish();
                            }

                            else{
                                Intent intent = new Intent(getApplicationContext(),userInterface.class);
//                                TaskStackBuilder.create(getApplicationContext()).addNextIntentWithParentStack(intent).startActivities();
                                startActivity(intent);
                                HomeActivity.this.finish();
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void toSignup(View view){
        Intent intent = new Intent(this,signup.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        else{
////            setContentView(R.layout.activity_main);
//            create=findViewById(R.id.create);
////            create.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    toSignup();
////                }
////            });
//        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 0);
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}