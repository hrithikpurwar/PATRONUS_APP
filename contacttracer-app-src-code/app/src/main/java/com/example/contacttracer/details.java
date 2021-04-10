package com.example.contacttracer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class details extends AppCompatActivity {
    TextView room_textView,floor_textView,block_textView;
    String room,block,floor;
    public void submitData(View view) throws JSONException {
        if(room_textView.length() == 0){
            Toast.makeText(this, "Room number cannot be empty", Toast.LENGTH_SHORT).show();

        }
        else if(floor_textView.length() == 0){
            Toast.makeText(this, "Floor number cannot be empty", Toast.LENGTH_SHORT).show();

        }
        else if(block_textView.length() == 0){
            Toast.makeText(this, "Block number cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            room="R".concat(room_textView.getText().toString());
            floor=floor_textView.getText().toString();
            block=block_textView.getText().toString();
            Map<String, String> user=new HashMap<String,String>();
            user.put("room",room);
            user.put("floor",floor);
            user.put("block",block);
//            JSONObject x=new JSONObject(user);

            ParseObject Udata=new ParseObject("data");
            Udata.put("username", ParseUser.getCurrentUser().getUsername());
            Udata.put("data",user.toString());
            Udata.put("room",room);
            Udata.put("floor",floor);
            Udata.put("block",block);
            Udata.put("Status","Green");
            Udata.put("Update_on","");

            List<String> contacts=new ArrayList<String>();
            Udata.put("contacts" , contacts);



            Udata.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Intent intent = new Intent(getApplicationContext(),userInterface.class);
//                        TaskStackBuilder.create(getApplicationContext()).addNextIntentWithParentStack(intent).startActivities();
                        startActivity(intent);
                        details.this.finish();
                    }
                    else{
                        Toast.makeText(details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ParseObject blockDB=new ParseObject(block);
            ParseQuery<ParseObject> query= ParseQuery.getQuery(block);
            query.whereEqualTo("floor",floor);
//            query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    Log.i("mine","Query started");
                    //If no data entry for this floor has been made
                    if (objects.size()==0 || objects==null){
                        blockDB.put("floor",floor);
                        Map<String, List<String>> roomData=new HashMap<>();
                        List<String> occupants=new ArrayList<>();
                        occupants.add(ParseUser.getCurrentUser().getUsername());
                        roomData.put(room,occupants);
//                        Log.i("mine",roomData.toString());
                        blockDB.put("floorData",roomData);
                        blockDB.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e!=null){
                                    Log.i("mine","Saving the block failed");
                                    e.printStackTrace();
                                    Toast.makeText(details.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Log.i("mine","Block db created and saved");

                                }
                            }
                        });
                    }
                    //if some data for this floor already exists
                    else{
                        Log.i("mine", "Floor data retrieved: "+objects.get(0).getString("floorData"));
                        try {
                            Log.i("mine","converting to Json");
                            ParseObject x= objects.get(0);
                            Map<String,List<String>> currFloor= (Map<String, List<String>>) x.get("floorData");
//                            List<String> roomData= (List<String>) currFloor.get(room,new ArrayList<String>());
                            Log.i("mine","converted to Json");
                            List<String> occupants=new ArrayList<String>();
                            if(currFloor.getOrDefault(room,null) == null){
                                Log.i("mine", "Room data dosent exist");

                            }
                            else {

//                                JSONArray arr= (JSONArray) currFloor.get(room);
//
//                                for(int i=0;i<arr.length();i++){
//                                    occupants.add(arr.get(i).toString());
//                                }
                                occupants=currFloor.get(room);

                            }
                            occupants.add(ParseUser.getCurrentUser().getUsername());
                            ParseObject object=objects.get(0);
                            currFloor.put(room,occupants);
                            Log.i("mine", String.valueOf(currFloor));
                            object.put("floorData",currFloor);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e!=null){
                                        e.printStackTrace();
                                        Toast.makeText(details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(details.this, "We have touchdown", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } catch (Exception jsonException) {
                            Toast.makeText(details.this, "oh shoot", Toast.LENGTH_SHORT).show();
                            jsonException.printStackTrace();
                            System.exit(0);
                        }
                    }
                    if(e!=null){
                        Log.i("mine","Query failed");
                        e.printStackTrace();
                        Toast.makeText(details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        room_textView=(TextView) findViewById(R.id.room);
         floor_textView=(TextView) findViewById(R.id.floor);
         block_textView=(TextView) findViewById(R.id.block);



    }
}