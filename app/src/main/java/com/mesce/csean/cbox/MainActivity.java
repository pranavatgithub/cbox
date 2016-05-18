package com.mesce.csean.cbox;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends Activity implements SensorEventListener, LocationListener {
    public boolean sendtruth = false;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 5000;
    private ProgressBar spinner;
    protected LocationManager locationManager;
    protected Context context;
    public double latitude;
    public double longitude;
    public String loc;
    public String noloc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);



        SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String value=(mSharedPreference.getString("key_name2", "Default_Value"));
        if(Objects.equals(value, "Default_Value"))
        {
            //Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),Startup.class);
            startActivity(intent);
            finish();
        }


        //Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
        ImageView imageView=(ImageView)findViewById(R.id.image);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        imageView.setImageResource(R.mipmap.log2);


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


       // Intent intent=new Intent(getApplicationContext(),Settings.class);
        //startActivity(intent);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {


                    if(!sendtruth) {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "crashed", duration);
                        toast.show();
                        sendmsg();

                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }

        }

    }

    public void sendmsg() {
        try {
            if(sendtruth==false) {
                Context context = getApplicationContext();
                String text = String.valueOf(latitude);
                String text2=String.valueOf(longitude);

                loc="http://maps.google.com/maps?q="+text+","+text2;

                final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String value = (mSharedPreference.getString("key_name1", "Default_Value"));

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(value, null, "car crashed at\n" + loc, null, null);

                //Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_LONG).show();
               Toast.makeText(getApplicationContext(), "location " + loc+"\t"+"sent to"+ value, Toast.LENGTH_LONG).show();
                finish();
            }


        }

        catch (Exception e) {
            final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String value=(mSharedPreference.getString("key_name1", "Default_Value"));
            //Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "location:" + loc + "sent", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void settings(MenuItem item) {
        Intent intent=new Intent(getApplicationContext(),Settings.class);
        startActivity(intent);
    }

    public void aboutus(MenuItem item) {
        Intent intent=new Intent(getApplicationContext(),aboutus.class);
        startActivity(intent);

    }
    @Override
    public void onLocationChanged(Location location) {

        latitude=location.getLatitude();
        longitude=location.getLongitude();


    }

    @Override
    public void onProviderDisabled(String provider) {


    }

    @Override
    public void onProviderEnabled(String provider) {


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}