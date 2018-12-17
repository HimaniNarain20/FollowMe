package com.example.followme;

import java.io.IOException;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ArduinoMain extends Activity {
    //Declare buttons & editText
    Button moveon;
    //Memeber Fields
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    LocationManager locationManager;

    // UUID service - This is the type of Bluetooth device that the BT module is
    // It is very likely yours will be the same, if not google UUID for your manufacturer
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module
    public String newAddress = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arduino_main);

        //  addKeyListener();

        //Initialising buttons in the view
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        moveon = (Button) findViewById(R.id.move);
        //getting the bluetooth adapter value and calling checkBTstate function
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        /**************************************************************************************************************************8
         *  Buttons are set up with onclick listeners so when pressed a method is called
         *  In this case send data is called with a value and a toast is made
         *  to give visual feedback of the selection made
         */

        moveon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getBaseContext(), "समन्वय भेज रहे है", Toast.LENGTH_SHORT).show();


                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // Toast.makeText(getApplicationContext(), "oooo", Toast.LENGTH_SHORT).show();


                if (ActivityCompat.checkSelfPermission(ArduinoMain.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ArduinoMain.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                       // locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

//                        try {
//                            Toast.makeText(getApplicationContext(), "no b", Toast.LENGTH_SHORT).show();
//                            Geocoder geocoder = new Geocoder(ArduinoMain.this, Locale.getDefault());
//                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                         //   locationText.setText(locationText.getText() + "\n" + addresses.get(0).getAddressLine(0) + ", " +
//                            //        addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2));
//                        } catch (Exception e) {
//
//                        }
String message=location.getLatitude()+","+location.getLongitude()+"#";
                        byte[] msgBuffer = message.getBytes();

                        try {
                            //attempt to place data on the outstream to the BT device
                            outStream.write(msgBuffer);
                        } catch (IOException e) {
                            //if the sending fails this is most likely because device is no longer there
                            Toast.makeText(getBaseContext(), "डिवाइस नहीं मिला", Toast.LENGTH_SHORT).show();
                            finish();
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


        @Override
        public void onResume () {
            super.onResume();
            // connection methods are best here in case program goes into the background etc

            //Get MAC address from DeviceListActivity
            Intent intent = getIntent();
            newAddress = intent.getStringExtra(PairPage.EXTRA_DEVICE_ADDRESS);

            // Set up a pointer to the remote device using its address.
            BluetoothDevice device = btAdapter.getRemoteDevice(newAddress);

            //Attempt to create a bluetooth socket for comms
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e1) {
                Toast.makeText(getBaseContext(), "ब्लूटूथ सॉकेट नहीं बना सका", Toast.LENGTH_SHORT).show();
            }

            // Establish the connection.
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();        //If IO exception occurs attempt to close socket
                } catch (IOException e2) {
                    Toast.makeText(getBaseContext(), "ब्लूटूथ सॉकेट बंद नहीं कर सका", Toast.LENGTH_SHORT).show();
                }
            }

            // Create a data stream so we can talk to the device
            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "ब्लूटूथ आउटस्ट्रीम नहीं बना सका", Toast.LENGTH_SHORT).show();
            }
            //When activity is resumed, attempt to send a piece of junk data ('x') so that it will fail if not connected
            // i.e don't wait for a user to press button to recognise connection failure
            String message="x";
            byte[] msgBuffer = message.getBytes();

            try {
                //attempt to place data on the outstream to the BT device
                outStream.write(msgBuffer);
            } catch (IOException e) {
                //if the sending fails this is most likely because device is no longer there
                Toast.makeText(getBaseContext(), "डिवाइस नहीं मिला", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onPause () {
            super.onPause();
            //Pausing can be the end of an app if the device kills it or the user doesn't open it again
            //close all connections so resources are not wasted

            //Close BT socket to device
            try {
                btSocket.close();
            } catch (IOException e2) {
                Toast.makeText(getBaseContext(), "ब्लूटूथ सॉकेट बंद करने में विफल", Toast.LENGTH_SHORT).show();
            }
        }
        //takes the UUID and creates a comms socket

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    //same as in device list activity
    private void checkBTState() {
        // Check device has Bluetooth and that it is turned on
        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "डिवाइस ब्लूटूथ का समर्थन नहीं करता है", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }


//        public void addKeyListener() {
//
//            // get edittext component
//            editText = (EditText) findViewById(R.id.editText1);
//
//            // add a keylistener to keep track user input
//            editText.setOnKeyListener(new OnKeyListener() {
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                    // if keydown and send is pressed implement the sendData method
//                    if ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
//                        //I have put the * in automatically so it is no longer needed when entering text
//                        sendData('*' + editText.getText().toString());
//                        Toast.makeText(getBaseContext(), "Sending text", Toast.LENGTH_SHORT).show();
//
//                        return true;
//                    }
//
//                    return false;
//                }
//            });
//        }


}


