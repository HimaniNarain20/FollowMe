package com.example.followme;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class StreamPage extends AppCompatActivity  {

    Button getLocationBtn;
    TextView locationText;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_stream_page);

        getLocationBtn = (Button) findViewById(R.id.get);
        locationText = (TextView) findViewById(R.id.text);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
               // Toast.makeText(getApplicationContext(), "oooo", Toast.LENGTH_SHORT).show();


                if (ActivityCompat.checkSelfPermission(StreamPage.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StreamPage.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {


                        Toast.makeText(getApplicationContext(), "granted" + location, Toast.LENGTH_SHORT).show();
                        locationText.setText("अक्षांश: " + location.getLatitude() + "\n देशान्तर: " + location.getLongitude());

                        try {
                            Toast.makeText(getApplicationContext(), "here", Toast.LENGTH_SHORT).show();
                            Geocoder geocoder = new Geocoder(StreamPage.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            locationText.setText(locationText.getText() + "\n" + addresses.get(0).getAddressLine(0) + ", " +
                                    addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2));
                        } catch (Exception e) {

                        }


                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }

        });
    }


}