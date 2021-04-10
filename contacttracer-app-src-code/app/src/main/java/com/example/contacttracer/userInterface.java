package com.example.contacttracer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class userInterface extends AppCompatActivity {
    TextView status_text;
    BluetoothAdapter adapter;
    LottieAnimationView animationView;
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logout();
                return false;
            }
        });
        popup.show();
    }



    public void logout(){
        ParseUser.logOut();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        TaskStackBuilder.create(getApplicationContext()).addNextIntentWithParentStack(intent).startActivities();
        startActivity(intent);
        userInterface.this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);

        ParseQuery<ParseObject>query=ParseQuery.getQuery("data");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
//        final String[] dataString = new String[1];
        String[] dataString = new String[1];
        final String[] statusData = new String[1];
        final ParseObject[] user = {null};
//        details_text=(TextView)findViewById(R.id.details);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject object :objects){
                    user[0] =object;
                    dataString[0] =object.getString("data");
                    statusData[0] =object.getString("Status");
                }
                try {
                    JSONObject data=new JSONObject(dataString[0]);
//                    JSONObject statusData=new JSONObject(statusString[0]);
//                    Log.i("mine",data.toString());

                    String room=data.getString("room");
                    String floor=data.getString("floor");
                    String block=data.getString("block");
                    try{
                        String updateDate=data.getString("update_on");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            SimpleDateFormat sdf = new SimpleDateFormat();
                            Calendar cal = Calendar.getInstance();
                            if(sdf.format(cal.getTime()).equals(updateDate)){
                                user[0].put("Status","Green");
                            }
                        }
                    }
                    catch(Exception ef){
                        ef.printStackTrace();
                    }
                    //Getting current date

                    String status=statusData[0];
//                    String details="Room: "+room+"\n"+"floor: "+floor+"\n"+"Block: "+block +"\n"+"Status: "+status;
//                    details_text.setText(details);
                    if(status.equals("Green")){
                        status_text=findViewById(R.id.greenText);
                        status_text.setVisibility(View.VISIBLE);
                        status_text=findViewById(R.id.greyText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.yellowText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.redText);
                        status_text.setVisibility(View.GONE);
                        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.green));

                        animationView=findViewById(R.id.greenAnimation);
                        animationView.setVisibility(View.VISIBLE);
                    }
                    else if(status.equals("grey")){
                        status_text=findViewById(R.id.greyText);
                        status_text.setVisibility(View.VISIBLE);
                        status_text=findViewById(R.id.yellowText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.redText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.greenText);
                        status_text.setVisibility(View.GONE);
                        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.grey));
                        animationView=findViewById(R.id.greyAnimation);
                        animationView.setVisibility(View.VISIBLE);
                    }
                    else if(status.equals("yellow")){
                        status_text=findViewById(R.id.yellowText);
                        status_text.setVisibility(View.VISIBLE);
                        status_text=findViewById(R.id.redText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.greenText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.greyText);
                        status_text.setVisibility(View.GONE);
                        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.yellow));

                        animationView=findViewById(R.id.yellowAnimation);
                        animationView.setVisibility(View.VISIBLE);
                    }
                    else if(status.equals("Red")){
                        status_text=findViewById(R.id.redText);
                        status_text.setVisibility(View.VISIBLE);
                        status_text=findViewById(R.id.greenText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.greyText);
                        status_text.setVisibility(View.GONE);
                        status_text=findViewById(R.id.yellowText);
                        status_text.setVisibility(View.GONE);
                        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.positive));
                        animationView=findViewById(R.id.redAnimation);
                        animationView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(userInterface.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        adapter=BluetoothAdapter.getDefaultAdapter();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
        }


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 0);
        }
        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

        }
        IntentFilter filter=new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);//tells us when we find a new device
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,filter);
        if( adapter.getScanMode()==BluetoothAdapter.SCAN_MODE_NONE){
            adapter.cancelDiscovery();
            adapter.startDiscovery();
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
            startActivity(discoverableIntent);
        }


    }
    private final BroadcastReceiver receiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action= intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                adapter.setName(ParseUser.getCurrentUser().getUsername());

            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name=device.getName();
                String address= device.getAddress();
                int rssi=(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                if(rssi>-250){
                    ParseQuery<ParseObject> query= ParseQuery.getQuery("data");
                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e!=null){
                                e.printStackTrace();
                            }
                            else if(objects==null || objects.size()==0){

                            }

                            else{
                                ParseObject obj=objects.get(0);
                                List<String> contacts=new ArrayList<>();
                                contacts.add(name);
                                obj.put("contacts",contacts);
                                obj.saveInBackground();
                            }

                        }
                    });
                }
            }
        }
    };
}