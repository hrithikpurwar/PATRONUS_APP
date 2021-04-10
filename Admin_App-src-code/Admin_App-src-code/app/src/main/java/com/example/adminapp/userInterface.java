package com.example.adminapp;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class userInterface extends AppCompatActivity {
    TextView id_text;
    String id;
    Button positiveButton,negativeButton;
    public void positive(View view){
        positiveButton.setEnabled(false);
        negativeButton.setEnabled(false);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("data");
        id=id_text.getText().toString();
        query.whereEqualTo("username",id);
        query.findInBackground((objects, e) -> {
            if(e!=null){
                e.printStackTrace();
                Toast.makeText(userInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            else if(objects == null|| objects.size()==0){

                Toast.makeText(userInterface.this, "No id found", Toast.LENGTH_SHORT).show();

            }
            else{
                ParseObject user=objects.get(0);
                user.put("Status","Red");

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            e.printStackTrace();
                            Toast.makeText(userInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }



                    }
                });
                String room=user.getString("room"),
                        floor=user.getString("floor"),
                        block=user.getString("block");
                ParseQuery<ParseObject> query1 =ParseQuery.getQuery("data");
                query1.whereEqualTo("block",block);
                query1.whereNotEqualTo("username",user.getString("username"));
                List<String> floormates=new ArrayList<String>(),
                        roommates=new ArrayList<String>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                //Getting current date
                Calendar cal = Calendar.getInstance();
                //Displaying current date in the desired format
//                System.out.println("Current Date: "+sdf.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_MONTH, 10);
                //Date after adding the days to the current date
                String newDate = sdf.format(cal.getTime());

                //Displaying the new Date after addition of Days to current date
//                System.out.println("Date after Addition: "+newDate);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e!=null){
                            e.printStackTrace();
                            Toast.makeText(userInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(objects==null || objects.size()==0 ){
//                            Toast.makeText(userInterface.this, "no blockmates", Toast.LENGTH_SHORT).show();
                        }
                        else{


                            for(ParseObject object:objects){
                                object.getString("username");
                                if(object.getString("room").equals(room)){
                                    roommates.add(object.getString("username"));
                                }
                                if(object.getString("floor").equals(floor)&& !roommates.contains(object.getString("username"))){
                                    floormates.add(object.getString("username"));
                                }


                            }
                            ParseQuery<ParseObject> FloormateQuery=ParseQuery.getQuery("data");
                            FloormateQuery.whereContainedIn("username",floormates);
                            FloormateQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e!=null){
                                        e.printStackTrace();
                                        Toast.makeText(userInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else if(objects==null || objects.size()==0 ){
//                                        Toast.makeText(userInterface.this, "no floormates", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        for(ParseObject object:objects){
                                                if(!object.getString("Status").equals("Red")) {
                                                    object.put("Status", "grey");
                                                    object.put("Update_on",newDate);
                                                    object.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {
                                                                Toast.makeText(userInterface.this, "Done", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                }
                                        }
                                    }

                                }
                            });

                            ParseQuery<ParseObject> roommateQuery=ParseQuery.getQuery("data");
                            roommateQuery.whereContainedIn("username",roommates);
                            roommateQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e!=null){
                                        Toast.makeText(userInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    else {
                                        for(ParseObject object:objects){
                                            if(!object.getString("Status").equals("Red")) {
                                                object.put("Status", "yellow");
                                                object.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Toast.makeText(userInterface.this, "Done", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });

                        }
                    }
                });


            }
            positiveButton.setEnabled(true);
            negativeButton.setEnabled(true);
        });
    }
    public void negative(View view){
        positiveButton.setEnabled(false);
        negativeButton.setEnabled(false);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("data");
        id=id_text.getText().toString();
        query.whereEqualTo("username",id);
        query.whereEqualTo("username",id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    Toast.makeText(userInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                else if(objects==null || objects.size()==0){
                    Toast.makeText(userInterface.this, "Invalid id", Toast.LENGTH_SHORT).show();
                }
                else{
                    ParseObject object=objects.get(0);
                    object.put("Status","Green");
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(userInterface.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        positiveButton.setEnabled(true);
        negativeButton.setEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        id_text=(TextView) findViewById(R.id.id);
        positiveButton=(Button)findViewById(R.id.positiveButton);
        negativeButton=(Button)findViewById(R.id.negativeButton);


    }
}