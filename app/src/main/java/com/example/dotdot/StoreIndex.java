package com.example.dotdot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class StoreIndex extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int RESQUEST_PERMISSION_LOCATION =1;
    // this variable get for the location in mobile
    private FusedLocationProviderClient mfusedLocationProviderClient;

    Button btn_dot;
    Button btn_notificaiton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_index);
        //--------------------------地圖------------------------------------------------------------
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //---------------------QRcode Scanner-------------------------------------------------------
        btn_dot = (Button) findViewById(R.id.btn_dot);

        btn_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(), QrcodeScanner.class);
                startActivity(j);
            } 
        });

        //------------------------Notification----------------------------------------------------
        btn_notificaiton = findViewById(R.id.storeNotification);
        btn_notificaiton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(getApplicationContext() , StoreNotification.class);
                startActivity(n);
            }
        });

    }
    //GMAP地圖--------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

/** this code is used to get the permission/ check the permission allow or not*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)

        {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.
                    ACCESS_FINE_LOCATION},RESQUEST_PERMISSION_LOCATION);
        }
        else
        {
            getMyLocation();
            Toast.makeText(this,"Permission is allowed",Toast.LENGTH_SHORT).show();
        }

        mMap.setMyLocationEnabled(true);
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    LatLng mylocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation,18));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== RESQUEST_PERMISSION_LOCATION){
            if (grantResults.length> 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getMyLocation();
            }
            else {
                Toast.makeText(this,"this is permission is mandatory",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},RESQUEST_PERMISSION_LOCATION);
            }
        }
    }
    //----------------------------------------------------------------------------------------------

}

