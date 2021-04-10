package com.example.contacttracer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class signup extends AppCompatActivity {
    String username,pwd,pwd2;
    TextView username_Text,pwd_text,pwd2_text;
    public void signup(View view){
        username=username_Text.getText().toString();
        pwd=pwd_text.getText().toString();
        pwd2=pwd2_text.getText().toString();
        if(username.length()==0){
            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();

        }
        else if(!pwd.equals(pwd2)|| pwd.length()==0){
            Toast.makeText(this, "Passwords don't match or invalid password", Toast.LENGTH_SHORT).show();
        }
        else{
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(pwd);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(signup.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),details.class);
//                        TaskStackBuilder.create(getApplicationContext()).addNextIntentWithParentStack(intent).startActivities();
                        startActivity(intent);
                        signup.this.finish();
                        //TODO: delete the main activity from stack
//                        MainActivity.getContext().finish();
                    }
                    else{
                        Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username_Text=(TextView) findViewById(R.id.newUname);
        pwd_text=(TextView) findViewById(R.id.newPwd);
        pwd2_text=(TextView) findViewById(R.id.newPwdConfirm);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}