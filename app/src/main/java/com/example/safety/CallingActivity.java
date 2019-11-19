package com.example.safety;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class CallingActivity extends AppCompatActivity implements LocationListener {

    private  EditText number1;
    private LocationManager locationManager;
    private Button save;
    private SharedPreferences numbers;
    public static final String call1 = "caller";
    StringBuilder data = new StringBuilder();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);
        number1 = findViewById(R.id.editText);

        save = findViewById(R.id.button);
        numbers = getSharedPreferences("calling_numbers",MODE_PRIVATE);
        //Saving Numbers
        number1.setText(numbers.getString("tag", "7986510416"));
        save.setOnClickListener(saveButtonListener);
//        call();
//        message();
        getLocation();

    }
    private void makeTag(String tag){
        String or = numbers.getString(tag, null);
        SharedPreferences.Editor preferencesEditor = numbers.edit();
        preferencesEditor.putString("tag",tag); //change this line to this
        preferencesEditor.commit();
        //call();


    }



    public View.OnClickListener saveButtonListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(number1.getText().length()>0){
                makeTag(number1.getText().toString());


                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(number1.getWindowToken(),0);

            }
        }

    };

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            double speed = location.getSpeed();
            data.append("I am in danger. Please help. This is my location. http://maps.google.com?q=");
            data.append(location.getLatitude());
            data.append(",");
            data.append(location.getLongitude());
            call();
            message();


        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(CallingActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    public void call()
    {
        String s = numbers.getString("tag",null);


        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91".concat(s)));//change the number
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }

    public void message()
    {

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);3
            String s = numbers.getString("tag",null);


            SmsManager smsManager = SmsManager.getDefault();
            String finalSms = data.toString();
            smsManager.sendTextMessage(s, null, finalSms, null, null);

            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
         //   finish();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();

        }
    }


}
